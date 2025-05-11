package com.tuyenvp.spring_boot_app.Controller.Home;

import com.tuyenvp.spring_boot_app.Model.*;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.*;
import com.tuyenvp.spring_boot_app.Services.Impl.EmailServiceImpl;
import com.tuyenvp.spring_boot_app.Services.Impl.ReviewServiceImpl;
import com.tuyenvp.spring_boot_app.Util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private AddressService addressService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private DbConnect DbConnect;
    @Autowired
    private ReviewServiceImpl reviewServiceImpl;
    @Autowired
    private EmailServiceImpl emailServiceImpl;

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
    public String addToCart(@RequestParam("vid") int variantId,
                            @RequestParam("uid") int userId,
                            HttpSession session) {
        // Lấy thông tin người dùng
        UserDtls user = DbConnect.userRepo.findById(userId).orElse(null);
        if (user == null) {
            session.setAttribute("errorMsg", "Vui lòng đăng nhập trước khi thêm vào giỏ hàng.");
            return "redirect:/login";
        }

        // Lấy biến thể sản phẩm
        ProductVariant variant = DbConnect.productVariantRepo.findById(variantId).orElse(null);
        if (variant == null) {
            session.setAttribute("errorMsg", "Biến thể sản phẩm không tồn tại.");
            return "redirect:/";
        }

        // Kiểm tra số lượng tồn kho
        if (variant.getStockQuantity() < 1) {
            session.setAttribute("errorMsg", "Sản phẩm đã hết hàng.");
            return "redirect:/detail_product/" + variant.getVariantId();
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Cart existingCartItem = DbConnect.cartRepo.findByProductVariantAndUser(variant.getVariantId(), user.getId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            existingCartItem = new Cart(user, variant, 1);
        }

        // Lưu vào database
        DbConnect.cartRepo.save(existingCartItem);

        // Thông báo cho người dùng
        session.setAttribute("succMsg", "Sản phẩm đã được thêm vào giỏ hàng.");

        // Chuyển hướng đến trang giỏ hàng
        return "redirect:/user/carts";
    }

    @GetMapping("/carts")
    public String showCart(Model model, HttpSession session, Principal principal) {
        // Lấy thông tin user từ session
        //UserDtls user = (UserDtls) session.getAttribute("user");
        if (principal == null) {
            session.setAttribute("errorMsg", "Bạn cần đăng nhập để xem giỏ hàng!");
            return "redirect:/login";
        }


        // Nếu người dùng đã đăng nhập
        String email = principal.getName(); // hoặc lấy từ AuthenticationPrincipal nếu bạn dùng Spring Security
        UserDtls user = DbConnect.userRepo.findByEmail(email);

        // Lấy danh sách sản phẩm trong giỏ hàng của user
        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);

        // Tính tổng tạm tính (subtotal)
        BigDecimal totalPrice = carts.stream()
                .map(cart -> cart.getProductVariant().getSellPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Lấy mã giảm giá từ session nếu có
        Discount discount = (Discount) session.getAttribute("appliedDiscount");
        BigDecimal discountValue = (discount != null) ? discount.getDiscountValue() : BigDecimal.ZERO;
        BigDecimal totalOrderPrice = totalPrice.subtract(discountValue);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("discountValue", discountValue);
        model.addAttribute("totalOrderPrice", totalOrderPrice);

        List<BreadCrumb> breadcrumb = List.of(
                new BreadCrumb("Giỏ hàng", "/user/carts", true)
        );
        model.addAttribute("breadcrumb", breadcrumb);

        return "user/carts"; // Trả về template cart.html
    }

    @PostMapping("/carts/applyDiscount")
    public String applyDiscount(@RequestParam("code") String code, Model model, HttpSession session, Principal principal) {
        // Kiểm tra người dùng đã đăng nhập chưa
        if (principal == null) {
            session.setAttribute("errorMsg", "Bạn cần đăng nhập để áp dụng mã giảm giá!");
            return "redirect:/login";
        }

        String email = principal.getName();
        UserDtls user = DbConnect.userRepo.findByEmail(email);

        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);

        // Tính tổng tạm tính (giá gốc)
        BigDecimal totalPrice = carts.stream()
                .map(cart -> cart.getProductVariant().getSellPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Lấy mã giảm giá từ bảng discount
        Discount discount = DbConnect.discountRepo.findByCode(code);

        if (discount != null && totalPrice.compareTo(discount.getMinAmount()) >= 0) {
            session.setAttribute("appliedDiscount", discount);
            session.setAttribute("succMsg", "Áp dụng mã giảm giá thành công!");
        } else {
            session.removeAttribute("appliedDiscount");
            session.setAttribute("errMsg", "Mã giảm giá không hợp lệ hoặc đơn hàng chưa đủ điều kiện.");
        }

        return "redirect:/user/carts";
    }

    @PostMapping("/carts/removeDiscount")
    public String removeDiscount(HttpSession session) {
        session.removeAttribute("appliedDiscount");
        return "redirect:/user/carts"; // quay lại trang giỏ hàng
    }

    // cập nhật số lượng trong giỏ hàng
    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Long cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/carts";
    }

    @PostMapping("/carts/remove")
    public String removeFromCart(@RequestParam("cartId") Long cartId, HttpSession session) {

        // Kiểm tra xem sản phẩm có tồn tại trong giỏ hàng của user không
        Optional<Cart> cart = DbConnect.cartRepo.findById(cartId);
        if (cart.isPresent()) {
            DbConnect.cartRepo.delete(cart.get());
        }

        return "redirect:/user/carts"; // Quay lại trang giỏ hàng
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

    // page order
    @GetMapping("/orders")
    public String orders(Model model, Principal principal, HttpSession session) {
        UserDtls user = getLoggedInUserDetails(principal);
        if(user==null){
            return "redirect:/login";
        }
        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);

        // Lay danh sách địa chỉ của người dùng
        List<Address> addresses = DbConnect.addressRepo.findByUser(user);
        model.addAttribute("addresses", addresses);

        // Tính tổng tạm tính (subtotal)
        BigDecimal totalPrice = carts.stream()
                .map(cart -> cart.getProductVariant().getSellPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Lấy mã giảm giá từ session nếu có
        Discount discount = (Discount) session.getAttribute("appliedDiscount");
        BigDecimal discountValue = (discount != null) ? discount.getDiscountValue() : BigDecimal.ZERO;
        BigDecimal totalOrderPrice = totalPrice.subtract(discountValue);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("discountValue", discountValue);
        model.addAttribute("totalOrderPrice", totalOrderPrice);

        List<BreadCrumb> breadcrumb = List.of(
                new BreadCrumb("Thanh toán", "/user/orders", true)
        );
        model.addAttribute("breadcrumb", breadcrumb);

        return "user/orders";
    }

    // Xử lý đặt hàng
    @PostMapping("/checkout")
    public String checkout(@RequestParam("paymentMethod") String paymentMethod,
                           Principal principal,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        String username = principal.getName();

        // Gọi service xử lý đơn hàng và trả về orderId
        Long orderId = orderService.placeOrder(paymentMethod, username, session);

        // Xử lý nếu có dấu phẩy (nhỡ truyền kiểu "bank,bank")
        if (paymentMethod != null && paymentMethod.contains(",")) {
            paymentMethod = paymentMethod.split(",")[0]; // lấy phần đầu tiên
        }
        paymentMethod = paymentMethod.trim().toLowerCase(); // chuẩn hóa

        // Chuyển sang trang thành công, truyền orderId và paymentMethod
        redirectAttributes.addAttribute("orderId", orderId);
        redirectAttributes.addAttribute("paymentMethod", paymentMethod);

        return "redirect:/user/success";

    }

    // Hiển thị trang đặt hàng thành công
    @GetMapping("/success")
    public String success(@RequestParam("orderId") Long orderId,
                          @RequestParam("paymentMethod") String paymentMethod,
                          Principal principal,
                          Model model) {
        UserDtls user = DbConnect.userRepo.findByEmail(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        // Đảm bảo chỉ lấy đúng 1 giá trị và chuẩn hóa
        if (paymentMethod != null) {
            paymentMethod = paymentMethod.trim().toLowerCase();
        }

        model.addAttribute("orderId", orderId);
        model.addAttribute("paymentMethod", paymentMethod);

        List<BreadCrumb> breadcrumb = List.of(
                new BreadCrumb("Thanh toán", "/user/orders", true),
                new BreadCrumb(orderId + " - Đơn hàng của bạn", "/user/success", false)
        );
        model.addAttribute("breadcrumb", breadcrumb);

        return "user/success";
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

        Order updateOrder = orderService.updateOrderStatus(id, status);

        try {
            emailServiceImpl.sendMailForOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", "Trạng thái được cập nhật");
        } else {
            session.setAttribute("errorMsg", "Trạng thái chưa được cập nhật");
        }
        return "redirect:/user/profile";
    }


    // page profile
    @GetMapping("/profile")
    public String profile(Model model, Principal p) {
        UserDtls user = getLoggedInUserDetails(p);
        if(user==null){
            return "redirect:/login";
        }
        List<Address> address = DbConnect.addressRepo.findByUser(user);
        model.addAttribute("address", address);
        List<Order> orders = orderService.getOrdersByUser(user.getId());
        model.addAttribute("orders", orders);

        List<BreadCrumb> breadcrumb = List.of(
                new BreadCrumb("T-member", "/user/profile", true)
        );
        model.addAttribute("breadcrumb", breadcrumb);
        return "user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute UserDtls user,
                                @RequestParam MultipartFile img,
                                HttpSession session) {
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

    // them dia chi
    @PostMapping("/profile/add-address")
    public String addAddress(@ModelAttribute Address address, Principal principal) {
        String email = principal.getName();
        UserDtls user = DbConnect.userRepo.findByEmail(email);
        address.setUser(user);
        if (address.isDefault()) {
            List<Address> existing = DbConnect.addressRepo.findByUser(user);
            for (Address a : existing) a.setDefault(false);
            DbConnect.addressRepo.saveAll(existing);
        }
        DbConnect.addressRepo.save(address);
        return "redirect:/user/profile"; // Quay về hiển thị danh sách
    }

    // xóa dia chi
    @GetMapping("/profile/delete/{addressId}")
    public String deleteAddress(@PathVariable("addressId") Long addressId, Principal principal) {
        String email = principal.getName();
        UserDtls user = DbConnect.userRepo.findByEmail(email);

        Address address = DbConnect.addressRepo.findById(addressId).orElse(null);
        if (address != null && address.getUser().getId().equals(user.getId())) {
            DbConnect.addressRepo.deleteById(addressId);
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/update-address")
    public String updateAddress(@ModelAttribute Address address, RedirectAttributes redirectAttributes, @RequestParam String activeTab) {
        addressService.updateAddress(address);
        redirectAttributes.addFlashAttribute("activeTab", activeTab);
        return "redirect:/user/profile";
    }

    // cap nhat dia chi mac dinh
    @GetMapping("/profile/set-default/{addressId}")
    public String setDefaultAddress(@PathVariable Long addressId, Principal principal) {
        String email = principal.getName();
        UserDtls user = DbConnect.userRepo.findByEmail(email);

        if (user == null) {
            // Xử lý lỗi nếu không tìm thấy user
            return "redirect:/login?error=user_not_found";
        }

        // reset địa chỉ mặc định cũ
        List<Address> addresses = DbConnect.addressRepo.findByUser(user);
        for (Address addr : addresses) {
            addr.setDefault(false);
            DbConnect.addressRepo.save(addr);
        }

        // set địa chỉ mới làm mặc định
        Address newDefault = DbConnect.addressRepo.findById(addressId).orElse(null);
        if (newDefault != null && newDefault.getUser().getId().equals(user.getId())) {
            newDefault.setDefault(true);
            DbConnect.addressRepo.save(newDefault);
        }

        return "redirect:/user/profile";
    }


    // trang tra cuu don hang
    @GetMapping("/track-order")
    public String track_order(){
        return "user/track-order";
    }

    @PostMapping("/track-order")
    public String trackOrder(@RequestParam("orderId") Long orderId, Model model) {
        Order order = orderService.findByOrderId(orderId);

        if (order == null) {
            model.addAttribute("message", "Không tìm thấy đơn hàng với mã: " + orderId);
            return "user/track-order";
        }

        List<String> statusList = Arrays.asList("Đặt hàng", "Xác nhận", "Đang giao", "Hoàn thành");
        int statusIndex = statusList.indexOf(order.getStatus());

        model.addAttribute("order", order);
        model.addAttribute("statusList", statusList);
        model.addAttribute("orderStatusIndex", statusIndex);
        return "redirect:/user/track-order";
    }

}
