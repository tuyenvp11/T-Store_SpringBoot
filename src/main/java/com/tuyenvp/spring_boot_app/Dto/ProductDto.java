package com.tuyenvp.spring_boot_app.Dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Data
public class ProductDto {
    private String productName;
    private Integer categoryId;
    private String brand;
    private String thumbnail;
    private String productDescrip;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer stockQuantity;
    private String releaseDate;

    // Thêm các thuộc tính từ bảng Specification
    private String screenSize;
    private String screenType;
    private String resolution;
    private String chipset;
    private String ram;
    private String storage;
    private String battery;
    private String charging;
    private String os;
    private String cameraMain;
    private String cameraFront;
    private String weight;
}
