package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.SizeProduct;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SizeProductService {
    List<SizeProduct> ListSizeProduct();
    Optional<SizeProduct> findSizeProductById(int size_id);
    SizeProduct addSizeProduct(SizeProduct add_size);
    SizeProduct updateSizeProduct(SizeProduct update_size);
    SizeProduct deleteSizeProduct(int size_id);
    List<SizeProduct> searchSizeProduct(String keyword);
    Page<SizeProduct> getAll(Integer pageNo);
    Page<SizeProduct> searchSizeProduct(String keyword, Integer pageNo);
}
