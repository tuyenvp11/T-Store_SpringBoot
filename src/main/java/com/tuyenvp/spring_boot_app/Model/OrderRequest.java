package com.tuyenvp.spring_boot_app.Model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

    private String firstName;

    private String lastName;

    private String email;

    private Integer phone;

    private String province;

    private String district;

    private String commune;

    private String address;

    private String paymentType;
}
