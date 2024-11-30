package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantService implements com.tuyenvp.spring_boot_app.Services.ProductVariantService {
    @Autowired
    private DbConnect DbConnect;
    @Override
    public List<ProductVariant> ListProductVariant() {
        return DbConnect.productVariantRepo.findAll();
    }

    @Override
    public Optional<ProductVariant> findProductVariantById(int variant_id) {
        return Optional.empty();
    }

    @Override
    public ProductVariant addProductVariant(ProductVariant add_variant) {
        return null;
    }

    @Override
    public ProductVariant updateProductVariant(ProductVariant edit_variant) {
        return null;
    }

    @Override
    public ProductVariant deleteProductVariant(int variant_id) {
        return null;
    }

    @Override
    public List<ProductVariant> searchProductVariant(String variant_name) {
        return List.of();
    }

    @Override
    public Page<ProductVariant> getAll(Pageable pageable) {
        return null;
    }
}
