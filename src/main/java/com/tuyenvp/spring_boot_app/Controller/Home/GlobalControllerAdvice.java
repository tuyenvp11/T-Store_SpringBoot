package com.tuyenvp.spring_boot_app.Controller.Home;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Services.CartService;
import com.tuyenvp.spring_boot_app.Services.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategory();
    }

    @ModelAttribute
    public void globalAttributes(Model model, HttpSession session) {
        UserDtls user = (UserDtls) session.getAttribute("user");

        model.addAttribute("user", user);

        if (user != null) {
            int countCart = cartService.getCountCart(user.getId());
            model.addAttribute("countCart", countCart);
        } else {
            model.addAttribute("countCart", 0);
        }
    }
}
