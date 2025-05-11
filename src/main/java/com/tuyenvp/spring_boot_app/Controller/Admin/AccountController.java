package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AccountController {

    @Autowired
    private UserService userService;

    /*@GetMapping("/account")
    public String getAllUsers(Model model, @RequestParam(required = false) Integer type,
                              @Param(value="keyword") String keyword,
                              @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
        if (type == null) {
            type = 1;
        }
        Page<UserDtls> list = userService.getAll(pageNo);
        if(keyword != null && !keyword.isEmpty()) {
            list = userService.searchUser(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        }else{
            list = userService.getAll(pageNo);
        }
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        List<UserDtls> users = (type == 1) ? userService.getUsers("ROLE_USER") : userService.getUsers("ROLE_ADMIN");
        model.addAttribute("userType", type);
        model.addAttribute("users", users);

        return "/admin/account";
    }*/

    @GetMapping("/account")
    public String getAllUsers(Model model,
                              @RequestParam(required = false) Integer type,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        if (type == null) {
            type = 1; // 1: user, 2: admin
        }

        Page<UserDtls> page;

        if (keyword != null && !keyword.isEmpty()) {
            page = userService.searchUser(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        } else {
            String role = (type == 1) ? "ROLE_USER" : "ROLE_ADMIN";
            page = userService.getUsersByRole(role, pageNo);
        }

        model.addAttribute("users", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("userType", type);

        return "/admin/account";
    }



    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,@RequestParam Integer type, HttpSession session) {
        Boolean f = userService.updateAccountStatus(id, status);
        if (f) {
            session.setAttribute("succMsg", "Trạng thái tài khoản đã được cập nhật");
        } else {
            session.setAttribute("errorMsg", "Cập nhật trạng thái tài khoản không thành công");
        }
        return "redirect:/admin/account";
    }

}
