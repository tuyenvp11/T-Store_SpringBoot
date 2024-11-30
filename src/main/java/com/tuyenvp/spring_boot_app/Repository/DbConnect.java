package com.tuyenvp.spring_boot_app.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbConnect {
    @Autowired
    public UserRepo userRepo;
    @Autowired
    public CategoryRepo categoryRepo;
    @Autowired
    public VendorRepo vendorRepo;
    @Autowired
    public ProductRepo productRepo;
    @Autowired
    public SizeProductRepo sizeProductRepo;
    @Autowired
    public ColorProductRepo colorProductRepo;
    @Autowired
    public ProductVariantRepo productVariantRepo;
    @Autowired
    public OrderRepo orderRepo;
    @Autowired
    public CartRepo cartRepo;

}

