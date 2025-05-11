package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class RegisterAdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/register_admin")
    public String register_admin(Model model) {
        model.addAttribute("user", new UserDtls());
        return "admin/register_admin";
    }

    @PostMapping("/save_admin")
    public String saveAdmin(@ModelAttribute UserDtls user, HttpSession session) throws IOException {

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            session.setAttribute("errorMsg", "Tên không được để trống");
            return "redirect:/admin/register_admin";
        }

        // Có thể thêm kiểm tra email, password...

        UserDtls saveUser = userService.saveAdmin(user);

        if (!ObjectUtils.isEmpty(saveUser)) {
            session.setAttribute("succMsg", "Đăng ký thành công");
        } else {
            session.setAttribute("errorMsg", "Có lỗi gì đó xảy ra trên server");
        }

        return "redirect:/admin/register_admin";
    }

}
