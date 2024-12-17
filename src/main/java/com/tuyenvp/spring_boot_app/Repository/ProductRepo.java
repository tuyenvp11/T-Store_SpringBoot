package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("SELECT c FROM Product c WHERE c.product_name LIKE %?1%")
    List<Product> searchProduct(String keyword);

    @Query("SELECT p FROM Product p WHERE " +
            "(p.product_name LIKE %:keyword% OR :keyword IS NULL) AND " +
            "(p.category.id = :categoryId OR :categoryId IS NULL)")
    List<Product> findByKeywordAndCategory(@Param("keyword") String keyword,
                                           @Param("categoryId") Integer categoryId);

    List<Product> findByCategory_id(Integer categoryId);
}
