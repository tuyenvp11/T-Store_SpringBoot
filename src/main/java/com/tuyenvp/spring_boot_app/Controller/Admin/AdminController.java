package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StatisticService statisticService;

    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalCategory", categoryService.getTotalCategory());
        model.addAttribute("totalProduct", productService.getTotalProducts());
        model.addAttribute("totalOrder", orderService.getTotalOrders());
        return "admin/index_admin";
    }

    @GetMapping("/account")
    public String getAllUsers(Model model, @RequestParam Integer type) {
        List<UserDtls> users = null;
        if (type == 1) {
            users = userService.getUsers("ROLE_USER");
        } else {
            users = userService.getUsers("ROLE_ADMIN");
        }
        model.addAttribute("userType",type);
        model.addAttribute("users", users);
        return "admin/account";
    }


    @GetMapping("/admin")
    public String showRevenueChart(Model model) {

        Map<String, Double> revenueByMonth = statisticService.getMonthlyRevenue();
        List<Object[]> topProducts = statisticService.getTopSellingProducts(5);

        model.addAttribute("months", revenueByMonth.keySet());
        model.addAttribute("revenues", revenueByMonth.values());

        List<String> productNames = topProducts.stream().map(row -> (String) row[0]).toList();
        List<Long> quantities = topProducts.stream().map(row -> ((Number) row[1]).longValue()).toList();

        model.addAttribute("topProductNames", productNames);
        model.addAttribute("topProductQuantities", quantities);

        model.addAttribute("productCount", statisticService.getProductCount());
        model.addAttribute("orderCount", statisticService.getOrderCount());
        model.addAttribute("userCount", statisticService.getUserCount());

        return "/admin/index_admin";
    }

}
