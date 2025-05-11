package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.Discount;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DiscountController{

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DbConnect DbConnect;

    @GetMapping("/discount")
    public String getDiscountPage(Model model) {
        List<Discount> discounts = discountService.getAllDiscount();
        model.addAttribute("discounts", discounts);
        return "admin/discount/discount";
    }

    @GetMapping("/discount/add_discount")
    public String add_discount(Model model) {
        Discount discount = new Discount();
        model.addAttribute("add_discount", discount);
        return "admin/discount/add_discount";
    }

    @PostMapping("/discount/add_discount")
    public String save_category(@ModelAttribute("discount") Discount discount) {
        DbConnect.discountRepo.save(discount);
        return "redirect:/admin/discount";
    }

    @GetMapping("/discount/edit_discount/{discountId}")
    public String edit_discount(@PathVariable("discountId")Long discountId, Model model) {
        Optional<Discount> discount = discountService.findByDiscountId(discountId);
        model.addAttribute("edit_discount", discount.get());
        return "admin/discount/edit_discount";
    }

    @PostMapping("/discount/edit_discount")
    public String update_discount(@ModelAttribute("edit_discount") Discount edit_discount) {
        discountService.updateDiscount(edit_discount);
        return "redirect:/admin/discount";
    }

    @GetMapping("/discount/del_discount/{discountId}")
    public String delete_discount(@PathVariable("discountId")Long discountId) {
        discountService.deleteDiscount(discountId);
        return "redirect:/admin/discount";
    }
}
