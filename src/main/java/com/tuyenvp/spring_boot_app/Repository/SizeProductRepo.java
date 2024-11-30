package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.SizeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SizeProductRepo extends JpaRepository<SizeProduct, Integer> {
    @Query("SELECT c FROM SizeProduct c WHERE c.size_name LIKE %?1%")
    List<SizeProduct> searchSizeProduct(String keyword);
}
