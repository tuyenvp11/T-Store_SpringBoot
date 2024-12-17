package com.tuyenvp.spring_boot_app.Controller.Home;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.CartService;
import com.tuyenvp.spring_boot_app.Services.CategoryService;
import com.tuyenvp.spring_boot_app.Services.ProductService;
import com.tuyenvp.spring_boot_app.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    DbConnect Dbconnect;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;

    // Lấy thông tin user
    @ModelAttribute
    public void getUserDetails(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            model.addAttribute("countCart", countCart);
        }

        /*List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);*/
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "category_id", required = false)
                        Integer category_id) {
        List<Product> products = productService.getAllProducts();
        if (category_id != null) {
            products = productService.getProductByCategory(category_id);
        }else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
            throws IOException {

        Boolean existsEmail = userService.existsEmail(user.getEmail());

        if (existsEmail) {
            session.setAttribute("errorMsg", "Email đã tồn tại");
        } else {
            String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
            user.setProfileImage(imageName);
            UserDtls saveUser = userService.saveUser(user);

            if (!ObjectUtils.isEmpty(saveUser)) {
                if (!file.isEmpty()) {
                    File saveFile = new ClassPathResource("static/fe/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                           + file.getOriginalFilename());

//					System.out.println(path);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                session.setAttribute("succMsg", "Đăng ký thành công");
            } else {
                session.setAttribute("errorMsg", "Lỗi gì đó xảy ra trên server");
            }
        }

        return "redirect:/register";
    }


    @GetMapping("/products")
    public String products(Model model,
                           @RequestParam(value = "category", defaultValue = "") String category,
                           @RequestParam(value = "category_id", required = false) Integer category_id,
                           @RequestParam(value="keyword", required = false) String keyword) {
        List<Product> products ;
        if (keyword != null && !keyword.isEmpty() && category_id != null) {
            products = productService.searchProductsByKeywordAndCategory(keyword, category_id);
        } else if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProduct(keyword);
            model.addAttribute("keyword", keyword);
        } else if (category_id != null) {
            products = productService.getProductByCategory(category_id);
        } else {
            products = productService.getAllProducts();
        }

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("category_id", category_id); // Giữ ID của danh mục được chọn
        model.addAttribute("paramValue", category);

        return "products";
    }


    @GetMapping("/detail_product/{product_id}")
    public String detail_product(@PathVariable int product_id, Model model, Principal principal) {
        Product productById = productService.getProductById(product_id);
        model.addAttribute("product", productById);
        if (principal != null) {
            String email = principal.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            model.addAttribute("countCart", countCart);
        }
        return "detail_product";
    }


}
