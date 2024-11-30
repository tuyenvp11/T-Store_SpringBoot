package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.ColorProduct;
import com.tuyenvp.spring_boot_app.Services.Impl.ColorProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping ("/admin")
public class ColorProductController {
    @Autowired
    private ColorProductService colorProductService;
    @GetMapping("/color_product")
    public String colorProduct(Model model) {
        List<ColorProduct> list = colorProductService.ListColorProduct();
        model.addAttribute("ListColorProduct", list);
        return "admin/color_product/color_product";
    }
}
