package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpecificationService {
    public Product getProductById(Integer productId);
}
