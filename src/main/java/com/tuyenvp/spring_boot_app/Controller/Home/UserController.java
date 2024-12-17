package com.tuyenvp.spring_boot_app.Controller.Home;

import com.tuyenvp.spring_boot_app.Model.Cart;
import com.tuyenvp.spring_boot_app.Model.OrderRequest;
import com.tuyenvp.spring_boot_app.Model.ProductOrder;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Services.CartService;
import com.tuyenvp.spring_boot_app.Services.OrderService;
import com.tuyenvp.spring_boot_app.Services.UserService;
import com.tuyenvp.spring_boot_app.Util.CommonUtil;
import com.tuyenvp.spring_boot_app.Util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model) {
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            if(userDtls!=null){
                model.addAttribute("user", userDtls);
                Integer countCart = cartService.getCountCart(userDtls.getId());
                model.addAttribute("countCart", countCart);
            }
            else{
                // Log hoặc xử lý nếu không tìm thấy người dùng
                System.out.println("Không tìm thấy người dùng với tên: " + email);
            }

        }

//        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
//        model.addAttribute("categorys", allActiveCategory);
    }

    // thêm vào giỏ hàng
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
        Cart saveCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Sản phẩm thêm thất bại");
        } else {
            session.setAttribute("succMsg", "Sản phẩm đã thêm vào giỏ hàng");
        }
        return "redirect:/detail_product/" + pid;
    }

    /*@GetMapping("/carts")
    public String loadCartPage(Principal principal, Model model) {
        if (principal == null) {
            System.out.println("Principal is null. Người dùng chưa đăng nhập.");
            model.addAttribute("errorMsg", "Vui lòng đăng nhập để xem giỏ hàng.");
            return "redirect:/user/carts";
        }

        UserDtls user = getLoggedInUserDetails(principal);

        if (user == null) {
            // Chuyển hướng hoặc trả về thông báo lỗi nếu người dùng không đăng nhập
            model.addAttribute("errorMsg", "Vui lòng đăng nhập để xem giỏ hàng.");
            return "redirect:/user/carts";
        }

        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);

        if (carts.size() > 0) {
        //if(carts != null && !carts.isEmpty()){
            BigDecimal totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
//            Double totalOrderPrice = carts.stream()
//                    .filter(cart -> cart.getTotalPrice() != null)
//                    .mapToDouble(Cart::getTotalOrderPrice)
//                    .sum(); // Tính tổng
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        else {
            model.addAttribute("totalOrderPrice", 0.0);
        }
        return "user/carts";
    }*/

    @GetMapping("/carts")
    public String loadCartPage(Principal principal, Model model) {
        List<Cart> carts = new ArrayList<>();
        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        if (principal != null) {
            // Lấy thông tin người dùng đăng nhập
            UserDtls user = getLoggedInUserDetails(principal);

            if (user != null) {
                // Lấy giỏ hàng của người dùng từ cơ sở dữ liệu
                carts = cartService.getCartByUser(user.getId());

                if (!carts.isEmpty()) {
                    // Tính tổng giá trị đơn hàng nếu có sản phẩm trong giỏ hàng
                    totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
                }
            }
        }

        // Nếu người dùng chưa đăng nhập hoặc giỏ hàng rỗng
        model.addAttribute("carts", carts);
        model.addAttribute("totalOrderPrice", totalOrderPrice);

        if (carts.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn đang trống.");
        }

        return "user/carts";
    }


    // cập nhật số lượng trong giỏ hàng
    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Long cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/carts";
    }

    // Kiểm tra người dùng đã đăng nhập
    private UserDtls getLoggedInUserDetails(Principal principal) {
        if(principal==null){
            return null;
        }
        String name = principal.getName();
        UserDtls user = userService.getUserByEmail(name);
        return user;
    }

    // page cart
    /*@GetMapping("/carts")
    public String cart(Model model, Principal principal) {
        if(principal == null) {
            return "redirect:/login";
        }
        String name = principal.getName();
        UserDtls userDtls = userServiceImpl.getUserByEmail(name);
        if (userDtls == null) {
            return "redirect:/login"; // Redirect if user details are not found
        }
        model.addAttribute("user", userDtls);
        List<Cart> carts = cartService.getCartByUser(userDtls.getId());
        model.addAttribute("carts", carts);
        return "user/carts";
    }*/

    // page order
    @GetMapping("/orders")
    public String orders(Model model, Principal p) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);
        if (carts.size() > 0) {
            BigDecimal orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            BigDecimal totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            model.addAttribute("orderPrice", orderPrice);
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/orders";
    }

    // Tiến hành thanh toán
    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p) throws Exception {
        // System.out.println(request);
        UserDtls user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(), request);

        return "redirect:/user/success";
    }

    // page order success
    @GetMapping("/success")
    public String success(Model model, Principal p) {
        UserDtls user = getLoggedInUserDetails(p);
        if(user==null){
            return "redirect:/login";
        }
        return "user/success";
    }


    // page user-order
    @GetMapping("/my-orders")
    public String myOrders(Model model, Principal p) {
        UserDtls user = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrdersByUser(user.getId());
        model.addAttribute("orders", orders);
        return "user/my-orders";
    }

    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);

        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", "Trạng thái được cập nhật");
        } else {
            session.setAttribute("errorMsg", "Trạng thái chưa được cập nhật");
        }
        return "redirect:/user/my-orders";
    }


    // page profile
    @GetMapping("/profile")
    public String profile(Model model, Principal p) {
        UserDtls user = getLoggedInUserDetails(p);
        if(user==null){
            return "redirect:/login";
        }
        return "user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile img, HttpSession session) {
        UserDtls updateUserProfile = userService.updateUserProfile(user, img);
        if (ObjectUtils.isEmpty(updateUserProfile)) {
            session.setAttribute("errorMsg", "Hồ sơ chưa được cập nhật");
        } else {
            session.setAttribute("succMsg", "Hồ sơ đã được cập nhật");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
                                 HttpSession session) {
        UserDtls loggedInUserDetails = getLoggedInUserDetails(p);

        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

        if (matches) {
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            UserDtls updateUser = userService.updateUser(loggedInUserDetails);
            if (ObjectUtils.isEmpty(updateUser)) {
                session.setAttribute("errorMsg", "Mật khẩu chưa được thay đổi thành công !! Lỗi máy chủ");
            } else {
                session.setAttribute("succMsg", "Mật khẩu đã thay đổi thành công");
            }
        } else {
            session.setAttribute("errorMsg", "Mật khẩu hiện tại không đúng");
        }

        return "redirect:/user/profile";
    }

}
