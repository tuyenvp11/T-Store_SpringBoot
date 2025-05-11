package com.tuyenvp.spring_boot_app.Controller.Home;

import com.tuyenvp.spring_boot_app.Model.*;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.*;
import com.tuyenvp.spring_boot_app.Services.Impl.EmailServiceImpl;
import com.tuyenvp.spring_boot_app.Services.Impl.ReviewServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailServiceImpl emailServiceImpl;
    @Autowired
    private ReviewService reviewService;

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
        model.addAttribute("categorys", allActiveCategory);*/
    }

    // lấy danh sách danh mục
    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "categoryId", required = false)
                        Integer categoryId) {
        List<Product> product = productService.getAllProducts();
        model.addAttribute("products", product);

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);

        List<Product> hotSaleProducts = product.stream().limit(5).collect(Collectors.toList());
        model.addAttribute("hotSaleProducts", hotSaleProducts);



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
    public String saveUser(@ModelAttribute UserDtls user, /*@RequestParam("img") MultipartFile file,*/ HttpSession session)
            throws IOException {

        Boolean existsEmail = userService.existsEmail(user.getEmail());

        if (existsEmail) {
            session.setAttribute("errorMsg", "Email đã tồn tại");
        } else {
            //String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
            //user.setProfileImage(imageName);
            UserDtls saveUser = userService.saveUser(user);

            if (!ObjectUtils.isEmpty(saveUser)) {
                /*if (!file.isEmpty()) {
                    File saveFile = new ClassPathResource("static/fe/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                           + file.getOriginalFilename());

//					System.out.println(path);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }*/
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
                           @RequestParam(value = "categoryId", required = false) Integer categoryId,
                           @RequestParam(value="keyword", required = false) String keyword) {
        List<Product> products = productService.getAllProducts();;

        if(keyword != null && !keyword.isEmpty()) {
            products = productService.searchProduct(keyword);
            model.addAttribute("keyword", keyword);
        }else{
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("brands", productService.getAllBrands());

        // link trang
        List<BreadCrumb> breadcrumb = List.of(
                new BreadCrumb("Sản phẩm", "/products", true)
        );
        model.addAttribute("breadcrumb", breadcrumb);

        return "products";
    }

    @GetMapping("/productCategory")
    public String listProductsByCategory(@RequestParam(value = "category", required = false) Integer categoryId,
                                         Model model) {
        List<Product> productCat;

        if (categoryId != null) {
            productCat = productService.getProductByCategoryId(categoryId);
        } else {
            productCat = productService.getAllProducts(); // fallback nếu không có category
        }

        model.addAttribute("productCat", productCat);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("selectedCategoryId", categoryId);

        return "/productCategory";
    }

    @GetMapping("/products/filter")
    public String filterProducts(@RequestParam(required = false) List<String> brand,
                                 @RequestParam(required = false) String priceRange,
                                 @RequestParam(required = false) Integer minPrice,
                                 @RequestParam(required = false) Integer maxPrice,
                                 Model model) {

        // Ưu tiên priceRange nếu có
        if (priceRange != null && !priceRange.isEmpty()) {
            String[] range = priceRange.split("-");
            minPrice = Integer.parseInt(range[0]);
            maxPrice = Integer.parseInt(range[1]);
        }

        if (minPrice == null) minPrice = 0;
        if (maxPrice == null) maxPrice = Integer.MAX_VALUE;

        List<Product> filtered;

        if (brand != null && !brand.isEmpty()) {
            filtered = new ArrayList<>();
            for (String b : brand) {
                filtered.addAll(productService.filterByBrandAndPrice(b, minPrice, maxPrice));
            }
        } else {
            filtered = productService.filterByBrandAndPrice(null, minPrice, maxPrice);
        }

        model.addAttribute("products", filtered);
        model.addAttribute("brands", productService.getAllBrands());
        model.addAttribute("selectedBrand", (brand != null && brand.size() == 1) ? brand.get(0) : null);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "products";
    }


    // xu ly lay san pham theo danh muc
    @GetMapping("/category/{categoryId}")
    public String productsByCategory(@PathVariable("categoryId") Integer categoryId, Model model) {
        List<Product> products = productService.getProductByCategoryId(categoryId);
        model.addAttribute("products", products);

        // Truyền danh sách danh mục nếu bạn dùng ở layout
        model.addAttribute("categories", categoryService.getAllCategory());

        return "products"; // tên file HTML hiển thị danh sách
    }

    @GetMapping("/detail_product/{productId}")
    public String detail_product(@PathVariable Integer productId, Model model, Principal principal) {
        Product product = productService.getProductById(productId);
        List<ProductVariant> variants = productVariantService.getVariantsByProduct(product);
        if (product == null) {
            throw new RuntimeException("San pham khong tim thay!");
        }
        //ProductVariant defaultVariant = variants.get(0);
        ProductVariant defaultVariant = variants.isEmpty() ? null : variants.get(0);

        List<Review> reviews = reviewService.getReviewsByVariant(defaultVariant.getVariantId());
        model.addAttribute("reviews", reviews);


        model.addAttribute("product", product);
        model.addAttribute("variants", variants);
        model.addAttribute("selectedVariant", defaultVariant);


        // xu ly nguoi dung khi them vao gio hang
        if (principal != null) {
            String email = principal.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            model.addAttribute("countCart", countCart);
        }

        List<BreadCrumb> breadcrumb = List.of(
                new BreadCrumb("Sản phẩm", "/products", false),
                new BreadCrumb(product.getProductName(), null, true)
        );

        model.addAttribute("breadcrumb", breadcrumb);

        return "detail_product";

    }

    @GetMapping("/detail_product/{productId}/color/{colorName}")
    public String productByColor(@PathVariable int productId,
                                 @PathVariable String colorName,
                                 Model model,
                                 Principal principal) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        List<ProductVariant> variants = productVariantService.getVariantsByProduct(product);
        ProductVariant selectedVariant = productVariantService.getVariantByProductAndColor(productId, colorName);
        if (selectedVariant == null) {
            throw new RuntimeException("Không tìm thấy biến thể cho màu " + colorName);
        }

        List<Review> reviews = reviewService.getReviewsByVariant(selectedVariant.getVariantId());

        model.addAttribute("product", product);
        model.addAttribute("variants", variants);
        model.addAttribute("selectedVariant", selectedVariant);
        model.addAttribute("reviews", reviews);

        // Gửi user nếu có
        if (principal != null) {
            String email = principal.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            model.addAttribute("countCart", countCart);
        }

        List<BreadCrumb> breadcrumb = List.of(
                new BreadCrumb("Sản phẩm", "/products", false),
                new BreadCrumb(product.getProductName() + " - Màu " + colorName, null, true)
        );

        model.addAttribute("breadcrumb", breadcrumb);

        return "detail_product";
    }


    @PostMapping("/review")
    public String submitReview(@RequestParam("variantId") Integer variantId,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {


        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để đánh giá.");
            return "redirect:/login";
        }
        String email = principal.getName();
        UserDtls user = userService.getUserByEmail(email);

        ProductVariant variant = Dbconnect.productVariantRepo.findById(variantId).orElse(null);
        if (variant == null) {
            redirectAttributes.addFlashAttribute("error", "Biến thể không tồn tại.");
            return "redirect:/";
        }

        Review review = new Review();
        review.setProductVariant(variant);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewTime(LocalDateTime.now());

        reviewService.saveReview(review);

        redirectAttributes.addFlashAttribute("success", "Đánh giá của bạn đã được gửi.");
        if (variant.getColor() != null) {
            String colorEncoded = URLEncoder.encode(variant.getColor().getColorName(), StandardCharsets.UTF_8).replace("+", "%20");
            return "redirect:/detail_product/" + variant.getProduct().getProductId() + "/color/" + colorEncoded;
        } else {
            return "redirect:/detail_product/" + variant.getProduct().getProductId();
        }    }

    // form quen mat khau
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        UserDtls user = Dbconnect.userRepo.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy tài khoản với email này!");
            return "forgot-password";
        }

        // Tạo token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        Dbconnect.userRepo.save(user);

        String resetLink = "http://localhost:8088/reset-password?token=" + token;

        // Gửi email
        String subject = "Yêu cầu đặt lại mật khẩu";
        String body = "Nhấn vào link sau để đặt lại mật khẩu: " + resetLink;
        emailServiceImpl.sendEmail(email, subject, body); // bạn cần tạo service gửi email

        model.addAttribute("message", "Đã gửi link đặt lại mật khẩu tới email của bạn.");
        return "forgot-password";
    }


    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        UserDtls user = Dbconnect.userRepo.findByResetToken(token);
        if (user == null) {
            model.addAttribute("error", "Token không hợp lệ!");
            return "reset-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }


    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("newPassword") String newPassword,
                                       Model model) {
        UserDtls user = Dbconnect.userRepo.findByResetToken(token);
        if (user == null) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
            return "redirect:/reset-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // Xoá token sau khi dùng
        Dbconnect.userRepo.save(user);

        model.addAttribute("message", "Mật khẩu của bạn đã được cập nhật!");
        return "login";
    }

    // trang cua hang gan ban
    @GetMapping("/store")
    public String store(Model model) {
        return "store";
    }

}
