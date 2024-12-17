package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return DbConnect.productRepo.save(add_product);
    }

    @Override
    public Product updateProduct(Product editProduct) {
        Optional<Product> product = DbConnect.productRepo.findById(editProduct.getProduct_id());
        if(product.isEmpty()) {
            return null;
        }
        Product updateProduct = product.get();
        updateProduct.setProduct_name(editProduct.getProduct_name());
        updateProduct.setCategory_id(editProduct.getCategory_id());
        updateProduct.setVendor_id(editProduct.getVendor_id());
        updateProduct.setProduct_img(editProduct.getProduct_img());
        updateProduct.setProduct_descrip(editProduct.getProduct_descrip());
        updateProduct.setProduct_price(editProduct.getProduct_price());
        updateProduct.setProduct_quantity(editProduct.getProduct_quantity());
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
    public Product getProductById(int product_id) {
        Product product = DbConnect.productRepo.findById(product_id).orElse(null);
        return product;
    }

    @Override
    public List<Product> getProductByCategory(int categoryId) {
        return DbConnect.productRepo.findByCategory_id(categoryId);
    }

    @Override
    public List<Product> searchProductsByKeywordAndCategory(String keyword, Integer categoryId) {
        return DbConnect.productRepo.findByKeywordAndCategory(keyword, categoryId);
    }
}
