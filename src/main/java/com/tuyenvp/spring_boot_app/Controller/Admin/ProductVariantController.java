package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.*;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ProductVariantController {
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ColorProductService colorProductService;
    @Autowired
    private SystemStorageService systemStorageService;
    @Autowired
    private DbConnect DbConnect;

    @GetMapping("/product_variant")
    public String productVariant(Model model,
                                 @Param(value="keyword") String keyword,
                                 @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
        Page<ProductVariant> list = productVariantService.getAll(pageNo);
        if(keyword != null && !keyword.isEmpty()) {
            list = productVariantService.searchProductVariant(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        }else{
            list = productVariantService.getAll(pageNo);
        }
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        model.addAttribute("ListProductVariant", list);
        return "admin/product_variant/product_variant";
    }

    // thêm bien the
    @GetMapping ("product_variant/add_variant")
    public String add_variant(Model model) {
        ProductVariant add_variant = new ProductVariant();
        model.addAttribute("add_variant", add_variant);

        List<Product> products = productService.getAllProducts();
        model.addAttribute("ListProduct", products);

        List<ColorProduct> colorProduct = colorProductService.getAllColorProducts();
        model.addAttribute("ListColorProduct", colorProduct);

        return "admin/product_variant/add_variant";
    }

    @PostMapping("product_variant/add_variant")
    public String save_variant(@ModelAttribute("productVariant") ProductVariant productVariant,
                               @RequestParam("img") MultipartFile file) {
        // upload file
        systemStorageService.store(file, "product");
        String fileName = file.getOriginalFilename();
        productVariant.setProductImg(fileName);

        // Tìm Color và Product từ database
        ColorProduct color = DbConnect.colorProductRepo.findByColorId(productVariant.getColor().getColorId())
                .orElseThrow(() -> new RuntimeException("Màu sắc không tồn tại"));

        Product product = DbConnect.productRepo.findByProductId(productVariant.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Lưu vào database
        productVariant.setColor(color);
        productVariant.setProduct(product);
        //productVariant.setSellPrice(product.getPrice());
        DbConnect.productVariantRepo.save(productVariant);

        return "redirect:/admin/product_variant";
    }

    // sửa sản phẩm
    @GetMapping("/product_variant/edit_variant/{variantId}")
    public String edit_variant(Model model, @ModelAttribute("variantId") Integer variantId) {
        Optional<ProductVariant> productVariant = productVariantService.findProductVariantById(variantId);
        model.addAttribute("edit_variant", productVariant.get());

        List<Product> products = productService.getAllProducts();
        model.addAttribute("ListProduct", products);

        List<ColorProduct> colorProduct = colorProductService.getAllColorProducts();
        model.addAttribute("ListColorProduct", colorProduct);

        return "admin/product_variant/edit_variant";
    }

    @PostMapping("/product_variant/edit_variant")
    public String update_variant(@ModelAttribute("edit_variant") ProductVariant productVariant, @RequestParam("img") MultipartFile file) {
        if(!file.isEmpty()) {
            systemStorageService.store(file, "product_variant");
            String fileName = file.getOriginalFilename();
            productVariant.setProductImg(fileName);
        }else {
            ProductVariant existingProductVariant = productVariantService.findProductVariantById(productVariant.getVariantId()).get();
            productVariant.setProductImg(existingProductVariant.getProductImg());
        }
        productVariantService.updateProductVariant(productVariant);
        return "redirect:/admin/product_variant";
    }

    @GetMapping("/product_variant/del_variant/{variantId}")
    public String del_variant(@PathVariable("variantId") Integer variantId) {
        productVariantService.deleteProductVariant(variantId);
        return "redirect:/admin/product_variant";
    }
}
