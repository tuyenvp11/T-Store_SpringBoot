package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface ProductService {
    List<Product> ListProduct();
    Optional<Product> findProductById(int product_id);
    Product addProduct(Product add_product);
    Product updateProduct(Product editProduct);
    Product deleteProduct(int product_id);
    List<Product> searchProduct(String keyword);
    Page<Product> getAll(Integer pageNo);
    Page<Product> searchProduct(String keyword, Integer pageNo);

    Product getProductById(int productId);
    List<Product> getAllProducts();
    List<Category> getAllCategories() ;
    List<Product> getProductByCategory(Category category);

    List<Product> getProductByCategoryId(Integer categoryId);

    long getTotalProducts();

    public List<Product> filterByBrandAndPrice(String brand, Integer minPrice, Integer maxPrice);

    public List<String> getAllBrands();


}
