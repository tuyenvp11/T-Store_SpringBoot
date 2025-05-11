package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.ColorProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorProductRepo extends JpaRepository<ColorProduct, Integer> {
    Optional<ColorProduct> findByColorId(Integer colorId);
    ColorProduct findByColorName(String colorName);
}
