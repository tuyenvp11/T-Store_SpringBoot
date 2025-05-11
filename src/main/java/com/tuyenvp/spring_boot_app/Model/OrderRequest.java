package com.tuyenvp.spring_boot_app.Model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@ToString
@Data
public class OrderRequest {

    /*private String receiverName;

    private String email;

    private String receiverPhone;

    private String fullAddress;

    private String paymentType;*/
    private List<OrderDetail> items; // Danh sách sản phẩm
    private Integer addressId;
    private String paymentType;
    private BigDecimal discountAmount;
}
