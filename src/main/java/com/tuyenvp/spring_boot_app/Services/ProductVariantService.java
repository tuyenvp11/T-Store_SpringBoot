package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductVariantService {
    List<ProductVariant> ListProductVariant(); ;
    Optional<ProductVariant> findProductVariantById(Integer variantId);


    ProductVariant addProductVariant(ProductVariant productVariant);
    List<ProductVariant> getVariantsByProduct(Product product);
    ProductVariant getVariantByProductAndColor(int productId, String colorName);

    List<ProductVariant> searchProductVariant(String keyword);
    Page<ProductVariant> getAll(Integer pageNo);
    Page<ProductVariant> searchProductVariant(String keyword, Integer pageNo);

    ProductVariant updateProductVariant(ProductVariant edit_variant);
    ProductVariant deleteProductVariant(int variant_id);
    //Page<ProductVariant> getAll(Pageable pageable);
}
