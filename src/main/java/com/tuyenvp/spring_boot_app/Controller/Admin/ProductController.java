package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.Category;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.Vendor;
import com.tuyenvp.spring_boot_app.Services.CategoryService;
import com.tuyenvp.spring_boot_app.Services.ProductService;
import com.tuyenvp.spring_boot_app.Services.SystemStorageService;
import com.tuyenvp.spring_boot_app.Services.VendorService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    public ProductService productService;
    @Autowired
    public CategoryService categoryService;
    @Autowired
    public VendorService vendorService;
    @Autowired
    private SystemStorageService systemStorageService;


    @GetMapping("/product")
    public String product(Model model,
                            @Param(value="keyword") String keyword,
                            @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
        Page<Product> list = productService.getAll(pageNo);
        if(keyword != null && !keyword.isEmpty()) {
            list = productService.searchProduct(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        }else{
            list = productService.getAll(pageNo);
        }
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        model.addAttribute("ListProduct", list);
        return "admin/product/product";
    }

    // thêm sản phẩm
    @GetMapping ("product/add_product")
    public String add_product(Model model) {
        Product add_product = new Product();
        model.addAttribute("add_product", add_product);

        List<Category> categories = categoryService.ListCategory();
        model.addAttribute("ListCategory", categories);

        List<Vendor> vendors = vendorService.ListVendor();
        model.addAttribute("ListVendor", vendors);
        return "admin/product/add_product";
    }
    @PostMapping("product/add_product")
    public String save_product(@ModelAttribute("product") Product product,
                               @RequestParam("img") MultipartFile file) {
        // upload file
        systemStorageService.store(file);
        String fileName = file.getOriginalFilename();
        product.setProduct_img(fileName);

        productService.addProduct(product);
        return "redirect:/admin/product";
    }

    // sửa sản phẩm
    @GetMapping("/product/edit_product/{product_id}")
    public String edit_product(Model model, @ModelAttribute("product_id") Integer product_id) {
        Optional<Product> product = productService.findProductById(product_id);
        model.addAttribute("edit_product", product.get());

        List<Category> categories = categoryService.ListCategory();
        model.addAttribute("ListCategory", categories);

        List<Vendor> vendors = vendorService.ListVendor();
        model.addAttribute("ListVendor", vendors);
        return "admin/product/edit_product";
    }

    @PostMapping("/product/edit_product")
    public String update_product(@ModelAttribute("edit_product") Product product, @RequestParam("img") MultipartFile file) {
        if(!file.isEmpty()) {
            systemStorageService.store(file);
            String fileName = file.getOriginalFilename();
            product.setProduct_img(fileName);
        }else {
            Product existingProduct = productService.findProductById(product.getProduct_id()).get();
            product.setProduct_img(existingProduct.getProduct_img());
        }
        productService.updateProduct(product);
        return "redirect:/admin/product";
    }


    @GetMapping("/product/del_product/{product_id}")
    public String del_product(@PathVariable("product_id") Integer product_id) {
        productService.deleteProduct(product_id);
        return "redirect:/admin/product";
    }


    // Xuat bao cao ra file excel
    @GetMapping("/product/export-products")
    public void exportProductsToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=san_pham_t-store.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Product> products = productService.getAllProducts(); // Lấy dữ liệu từ DB
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã sản phẩm");
        headerRow.createCell(1).setCellValue("Tên sản phẩm");
        headerRow.createCell(2).setCellValue("Danh mục");
        headerRow.createCell(3).setCellValue("Nhà cung cấp");
        headerRow.createCell(4).setCellValue("Hình ảnh");
        headerRow.createCell(5).setCellValue("Mô tả sản phẩm");
        headerRow.createCell(6).setCellValue("Giá sản phẩm");
        headerRow.createCell(7).setCellValue("Tổng số lượng nhập");

        int rowCount = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(product.getProduct_id());
            row.createCell(1).setCellValue(product.getProduct_name());
            row.createCell(2).setCellValue(product.getCategory().getCategory_name());
            row.createCell(3).setCellValue(product.getVendor().getVendor_name());
            row.createCell(4).setCellValue(product.getProduct_img());
            row.createCell(5).setCellValue(product.getProduct_descrip());
            row.createCell(6).setCellValue(product.getProduct_price());
            row.createCell(7).setCellValue(product.getProduct_quantity());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}

