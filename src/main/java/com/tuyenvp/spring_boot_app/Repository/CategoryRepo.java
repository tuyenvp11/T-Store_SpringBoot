package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.categoryName LIKE %?1%")
    List<Category> searchCategory(String keyword);

    Optional<Category> findByCategoryName(String name);

    boolean existsByCategoryName(String name); // Kiểm tra danh mục đã tồn tại chưa
}
