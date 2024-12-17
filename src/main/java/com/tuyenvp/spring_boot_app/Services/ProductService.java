package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    public List<Product> ListProduct();
    Optional<Product> findProductById(int product_id);
    public Product addProduct(Product add_product);
    public Product updateProduct(Product editProduct);
    public Product deleteProduct(int product_id);
    public List<Product> searchProduct(String keyword);
    Page<Product> getAll(Integer pageNo);
    Page<Product> searchProduct(String keyword, Integer pageNo);
    public Product getProductById(int product_id);
    public List<Product> getAllProducts();
    public List<Product> getProductByCategory(int categoryId);
    public List<Product> searchProductsByKeywordAndCategory(String keyword, Integer categoryId);
    public long getTotalProducts();
}
