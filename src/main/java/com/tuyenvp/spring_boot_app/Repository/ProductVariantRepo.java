package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.ColorProduct;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepo extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> findByProductProductId(Integer productId);
    List<ProductVariant> findByProduct(Product product);
    ProductVariant findByProductAndColor(Product product, ColorProduct colorProduct);

    @Query("SELECT c FROM ProductVariant c WHERE LOWER(c.product.productName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<ProductVariant> searchProductVariant(String keyword);
}
