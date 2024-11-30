package com.tuyenvp.spring_boot_app.Controller;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Services.Impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class TestAPI {
    @Autowired
    public ProductServiceImpl productServiceImpl;
    @GetMapping
    public List<Product> getProducts() {
        return productServiceImpl.ListProduct();
    }


}
