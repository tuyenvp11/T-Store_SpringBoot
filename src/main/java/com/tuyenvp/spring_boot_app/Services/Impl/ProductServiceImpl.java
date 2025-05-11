package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.Specification;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private DbConnect DbConnect;

    @Override
    public List<Product> ListProduct() {
        return DbConnect.productRepo.findAll();
    }

    @Override
    public Optional<Product> findProductById(int product_id) {
        return DbConnect.productRepo.findById(product_id);
    }

    @Override
    public Product addProduct(Product add_product) {
        // Tạo Specification từ dữ liệu của add_product
        Specification spec = add_product.getSpecification();

        return DbConnect.productRepo.save(add_product);
    }

    @Override
    public Product updateProduct(Product editProduct) {
        Optional<Product> product = DbConnect.productRepo.findById(editProduct.getProductId());
        if(product.isEmpty()) {
            return null;
        }
        Product updateProduct = product.get();
        updateProduct.setProductName(editProduct.getProductName());
        updateProduct.setCategoryId(editProduct.getCategoryId());
        updateProduct.setBrand(editProduct.getBrand());
        updateProduct.setThumbnail(editProduct.getThumbnail());
        updateProduct.setProductDescrip(editProduct.getProductDescrip());
        updateProduct.setOriginalPrice(editProduct.getOriginalPrice());
        updateProduct.setPrice(editProduct.getPrice());
        updateProduct.setReleaseDate(editProduct.getReleaseDate());
        updateProduct.setWarranty(editProduct.getWarranty());
        DbConnect.productRepo.save(updateProduct);
        return updateProduct;
    }

    @Override
    public Product deleteProduct(int product_id) {
        Optional<Product> product = DbConnect.productRepo.findById(product_id);
        if(product.isEmpty()) {
            return null;
        }
        DbConnect.productRepo.deleteById(product_id);
        return product.get();
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        return DbConnect.productRepo.searchProduct(keyword);
    }

    @Override
    public Page<Product> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        return this.DbConnect.productRepo.findAll(pageable);
    }

    public List<Product> getAllProducts() {
        return DbConnect.productRepo.findAll();
    }


    @Override
    public long getTotalProducts() {
        return DbConnect.productRepo.count();
    }

    @Override
    public List<Product> filterByBrandAndPrice(String brand, Integer minPrice, Integer maxPrice) {
        if (minPrice == null) minPrice = 0;
        if (maxPrice == null) maxPrice = Integer.MAX_VALUE;

        BigDecimal min = BigDecimal.valueOf(minPrice);
        BigDecimal max = BigDecimal.valueOf(maxPrice);

        if (brand == null || brand.isEmpty()) {
            return DbConnect.productRepo.findByPriceBetween(min, max);
        } else {
            return DbConnect.productRepo.findByBrandAndPriceBetween(brand, min, max);
        }
    }


    @Override
    public List<String> getAllBrands() {
        return DbConnect.productRepo.findDistinctBrand();
    }


    @Override
    public Page<Product> searchProduct(String keyword, Integer pageNo) {
        List list = searchProduct(keyword);
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end =(int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                ? list.size()
                : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);

        return new PageImpl<Product>(list, pageable, searchProduct(keyword).size());
    }

    @Override
    public Product getProductById(int productId) {
        Product product = DbConnect.productRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("San pham khong tim thay"));
        return product;
    }

    @Override
    public List<Product> getProductByCategory(Category category) {
        return DbConnect.productRepo.findByCategory(category);
    }

    @Override
    public List<Product> getProductByCategoryId(Integer categoryId) {
        return DbConnect.productRepo.findByCategory_CategoryId(categoryId);
    }


    @Override
    public List<Category> getAllCategories() {
        return DbConnect.categoryRepo.findAll();
    }

}
