package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductVariantService {
    List<ProductVariant> ListProductVariant(); ;
    Optional<ProductVariant> findProductVariantById(int variant_id);
    ProductVariant addProductVariant(ProductVariant add_variant);
    ProductVariant updateProductVariant(ProductVariant edit_variant);
    ProductVariant deleteProductVariant(int variant_id);
    List<ProductVariant> searchProductVariant(String variant_name);
    Page<ProductVariant> getAll(Pageable pageable);
}
