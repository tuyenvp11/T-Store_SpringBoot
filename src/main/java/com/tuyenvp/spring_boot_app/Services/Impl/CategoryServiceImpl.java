package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private DbConnect DbConnect;

    @Override
    public List<Category> ListCategory() {
        return DbConnect.categoryRepo.findAll();
    }

    @Override
    public Optional<Category> findCategoryById(int category_id) {
        return DbConnect.categoryRepo.findById(category_id);
    }

    @Override
    public String addCategory(Category add_category) {
        Optional<Category> existingCategory = DbConnect.categoryRepo.findByCategoryName(add_category.getCategoryName());
        if (existingCategory.isPresent()) {
            return "Danh mục đã tồn tại!";
        }
        DbConnect.categoryRepo.save(add_category);
        return "Thêm danh mục thành công!";

    }

    @Override
    public Category updateCategory(Category edit_category) {
        Optional<Category>category = DbConnect.categoryRepo.findById(edit_category.getCategoryId());
        if (category.isEmpty()) {
            return null;
        }
        Category update_category = category.get();
        // cập nhật tên danh mục
        update_category.setCategoryName(edit_category.getCategoryName());
        update_category.setIconUrl(edit_category.getIconUrl());
        DbConnect.categoryRepo.save(update_category);
        return update_category;
    }

    @Override
    public Category deleteCategory(int category_id) {
        Optional<Category>category = DbConnect.categoryRepo.findById(category_id);
        if(category.isEmpty()) {
            return null;
        }
        DbConnect.categoryRepo.deleteById(category_id);
        return category.get();
    }

    @Override
    public List<Category> searchCategory(String keyword) {
        return DbConnect.categoryRepo.searchCategory(keyword);
    }

    @Override
    public Page<Category> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        return DbConnect.categoryRepo.findAll(pageable);
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = DbConnect.categoryRepo.findAll();
        return categories;
    }

    @Override
    public Page<Category> searchCategory(String keyword, Integer pageNo) {
        List list = searchCategory(keyword);
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        Integer start = (int) pageable.getOffset();
        Integer end =(int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                            ? list.size()
                            : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Category>(list, pageable, searchCategory(keyword).size());
    }

    @Override
    public long getTotalCategory() {
        return DbConnect.categoryRepo.count();
    }

    public boolean isCategoryExists(String name) {
        return DbConnect.categoryRepo.existsByCategoryName(name);
    }
    /*@Override
    public Category getCategoryById(int category_id) {
        return DbConnect.categoryRepo.getById(category_id);
    }*/

}
