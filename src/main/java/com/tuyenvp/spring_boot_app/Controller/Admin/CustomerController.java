package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Services.OrderService;
import com.tuyenvp.spring_boot_app.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    private UserService userService;

    @GetMapping("/customer")
    public String getAllUsers(Model model, @RequestParam(required = false) Integer type) {
        if (type == null) {
            type = 1; // Default to ROLE_USER if no type is provided
        }
        List<UserDtls> users = (type == 1) ? userService.getUsers("ROLE_USER") : userService.getUsers("ROLE_ADMIN");
        model.addAttribute("userType", type);
        model.addAttribute("users", users);

        return "admin/customer";
    }

}
