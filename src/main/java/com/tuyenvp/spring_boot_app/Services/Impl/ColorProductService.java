package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.ColorProduct;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorProductService implements com.tuyenvp.spring_boot_app.Services.ColorProductService {
    @Autowired
    private DbConnect Dbconnect;
    @Override
    public List<ColorProduct> ListColorProduct() {
        return Dbconnect.colorProductRepo.findAll();
    }

    @Override
    public Optional<ColorProduct> findColorProductById(int color_id) {
        return Optional.empty();
    }

    @Override
    public ColorProduct addColorProduct(ColorProduct add_color) {
        return null;
    }

    @Override
    public ColorProduct updateColorProduct(ColorProduct update_color) {
        return null;
    }

    @Override
    public ColorProduct deleteColorProduct(int color_id) {
        return null;
    }

    @Override
    public List<ColorProduct> searchColorProduct(String keyword) {
        return List.of();
    }

    @Override
    public Page<ColorProduct> getAll(Integer pageNo) {
        return null;
    }

    @Override
    public Page<ColorProduct> searchColorProduct(Pageable pageable, String keyword) {
        return null;
    }
}
