package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.SizeProduct;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.SizeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeProductServiceImpl implements SizeProductService {
    @Autowired
    private DbConnect Dbconnect;
    @Override
    public List<SizeProduct> ListSizeProduct() {
        return Dbconnect.sizeProductRepo.findAll();
    }

    @Override
    public Optional<SizeProduct> findSizeProductById(int size_id) {
        return Optional.empty();
    }

    @Override
    public SizeProduct addSizeProduct(SizeProduct add_size) {
        return null;
    }

    @Override
    public SizeProduct updateSizeProduct(SizeProduct update_size) {
        return null;
    }

    @Override
    public SizeProduct deleteSizeProduct(int size_id) {
        return null;
    }

    @Override
    public List<SizeProduct> searchSizeProduct(String keyword) {
        return Dbconnect.sizeProductRepo.searchSizeProduct(keyword);
    }

    @Override
    public Page<SizeProduct> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        return Dbconnect.sizeProductRepo.findAll(pageable);
    }

    @Override
    public Page<SizeProduct> searchSizeProduct(String keyword, Integer pageNo) {
        List list = searchSizeProduct(keyword);
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        Integer start = (int) pageable.getOffset();
        Integer end =(int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                ? list.size()
                : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<SizeProduct>(list, pageable, list.size());
    }
}
