package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.CategoryService;
import com.tuyenvp.spring_boot_app.Services.SystemStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DbConnect DbConnect;

    @Autowired
    private SystemStorageService systemStorageService;

    @GetMapping("/category")
    public String category(Model model,
                           @Param(value="keyword") String keyword ,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<Category>list = categoryService.getAll(pageNo);

        if(keyword != null && !keyword.isEmpty()) {
            list = categoryService.searchCategory(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        }else{
            list = categoryService.getAll(pageNo);
        }

        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("ListCategory",list);
        return "admin/category/category";
    }

    // thêm danh mục
    @GetMapping(value = "/category/add_category")
    public String add_category(Model model){
        Category add_category = new Category();
        model.addAttribute("add_category", add_category);
        return "admin/category/add_category";
    }
    @PostMapping("/category/add_category")
    public String save_category(Model model,@ModelAttribute("category") Category category ,
                                @RequestParam("img") MultipartFile file) {
        /*if(bindingResult.hasErrors()) {
            session.setAttribute("succMsg", "Thêm danh mục không thành công!");
            return "admin/category/add_category";
        }*/
        // upload file
        systemStorageService.store(file, "category");
        String fileName = file.getOriginalFilename();
        category.setIconUrl(fileName);

        DbConnect.categoryRepo.save(category);
        //categoryService.addCategory(save_category);
        return "redirect:/admin/category";
    }
    // sửa danh mục
    @GetMapping("/category/edit_category/{categoryId}")
    public String edit_category(Model model, @PathVariable("categoryId")Integer categoryId){
        Optional<Category>category = categoryService.findCategoryById(categoryId);
        model.addAttribute("edit_category", category.get());
        return "admin/category/edit_category";
    }
    @PostMapping("/category/edit_category")
    public String update_category(@Valid @ModelAttribute("edit_category") Category category,
                                  BindingResult bindingResult, @RequestParam("img")
                                  MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "admin/category/edit_category";
        }
        if(!file.isEmpty()) {
            systemStorageService.store(file, "category");
            String fileName = file.getOriginalFilename();
            category.setIconUrl(fileName);
        }else {
            Category existingCategory = categoryService.findCategoryById(category.getCategoryId()).get();
            category.setIconUrl(existingCategory.getIconUrl());
        }
        categoryService.updateCategory(category);
        return "redirect:/admin/category";
    }

    @GetMapping("/category/del_category/{id}")
    public String del_category(@PathVariable("id")Integer id){
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }
}
