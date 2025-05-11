package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    @Column(name = "code", nullable = false)
    private String code; // Mã giảm giá

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue; // Giá trị giảm

    private BigDecimal minAmount;  // Số tiền tối thiểu áp dụng mã giảm giá

    private BigDecimal maxAmount;  // Số tiền tối đa áp dụng mã giảm giá (nếu có)


}
