package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("SELECT c FROM Product c WHERE LOWER(c.productName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Product> searchProduct(String keyword);

    @Query("SELECT p FROM Product p WHERE " +
            "(p.productName LIKE %:keyword% OR :keyword IS NULL) AND " +
            "(p.category.categoryId = :categoryId OR :categoryId IS NULL)")
    List<Product> findByKeywordAndCategory(@Param("keyword") String keyword,
                                           @Param("categoryId") Integer categoryId);

    List<Product> findByCategory(@Param("product") Category category);

    Optional<Product> findByProductId(Integer productId);

    //@Query("SELECT p FROM Product p WHERE p.category.categoryId = ?1")
    List<Product> findByCategory_CategoryId(Integer categoryId);

    // loc san pham theo gia
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findByBrandAndPriceBetween(String brand, BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findDistinctBrand();

}
