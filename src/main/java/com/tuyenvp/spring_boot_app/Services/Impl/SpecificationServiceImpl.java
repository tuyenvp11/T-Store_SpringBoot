package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.Specification;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private DbConnect DbConnect;


    @Override
    public Product getProductById(Integer productId) {
        return DbConnect.productRepo.findById(productId).orElseThrow(()
                -> new RuntimeException("Sản phẩm không tồn tại"));
    }
}
