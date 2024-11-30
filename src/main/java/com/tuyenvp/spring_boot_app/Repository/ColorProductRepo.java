package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.ColorProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorProductRepo extends JpaRepository<ColorProduct, Integer> {
}
