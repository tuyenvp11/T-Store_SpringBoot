package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.SizeProduct;
import com.tuyenvp.spring_boot_app.Services.Impl.SizeProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class SizeProductController {
    @Autowired
    private SizeProductServiceImpl sizeProductServiceImpl;
    @GetMapping("/size_product")
    public String sizeProduct(Model model,
                              @Param(("keyword")) String keyword,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        //List<SizeProduct> list = sizeProductService.ListSizeProduct();
        Page<SizeProduct> list = sizeProductServiceImpl.getAll(pageNo);
        if(keyword != null && !keyword.isEmpty()) {
            list = sizeProductServiceImpl.searchSizeProduct(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        }else{
            list = sizeProductServiceImpl.getAll(pageNo);
        }
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("ListSizeProduct", list);
        return "admin/size_product/size_product";
    }
}
