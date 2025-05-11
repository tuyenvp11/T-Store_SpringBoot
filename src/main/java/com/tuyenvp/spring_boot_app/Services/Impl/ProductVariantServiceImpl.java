package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.ColorProduct;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.ColorProductService;
import com.tuyenvp.spring_boot_app.Services.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {
    @Autowired
    private DbConnect DbConnect;

    @Autowired
    private ColorProductService colorProductService;

    @Override
    public List<ProductVariant> ListProductVariant() {
        return DbConnect.productVariantRepo.findAll();
    }

    @Override
    public Optional<ProductVariant> findProductVariantById(Integer variantId) {
        return DbConnect.productVariantRepo.findById(variantId);
    }



    @Override
    public ProductVariant addProductVariant(ProductVariant productVariant) {
        return DbConnect.productVariantRepo.save(productVariant);
    }

    @Override
    public List<ProductVariant> getVariantsByProduct(Product product) {
        return DbConnect.productVariantRepo.findByProduct(product);
    }

    @Override
    public ProductVariant getVariantByProductAndColor(int productId, String colorName) {
        ColorProduct color = DbConnect.colorProductRepo.findByColorName(colorName);
        if (color == null) return null;

        Product product = new Product();
        product.setProductId(productId);
        return DbConnect.productVariantRepo.findByProductAndColor(product, color);
    }

    @Override
    public ProductVariant updateProductVariant(ProductVariant edit_variant) {
        return null;
    }

    @Override
    public ProductVariant deleteProductVariant(int variant_id) {
        return null;
    }

    @Override
    public List<ProductVariant> searchProductVariant(String keyword) {
        return DbConnect.productVariantRepo.searchProductVariant(keyword);
    }

    @Override
    public Page<ProductVariant> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        return this.DbConnect.productVariantRepo.findAll(pageable);
    }

    @Override
    public Page<ProductVariant> searchProductVariant(String keyword, Integer pageNo) {
        List list = searchProductVariant(keyword);
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end =(int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                ? list.size()
                : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);

        return new PageImpl<ProductVariant>(list, pageable, searchProductVariant(keyword).size());
    }
}
