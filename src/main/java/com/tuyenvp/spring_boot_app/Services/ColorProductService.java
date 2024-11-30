package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.ColorProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ColorProductService {
    List<ColorProduct> ListColorProduct();
    Optional<ColorProduct> findColorProductById(int color_id);
    ColorProduct addColorProduct(ColorProduct add_color);
    ColorProduct updateColorProduct(ColorProduct update_color);
    ColorProduct deleteColorProduct(int color_id);
    List<ColorProduct> searchColorProduct(String keyword);
    Page<ColorProduct> getAll(Integer pageNo);
    Page<ColorProduct> searchColorProduct(Pageable pageable, String keyword);
}
