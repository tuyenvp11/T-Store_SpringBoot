package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "variant_id")
    private Integer variantId;

    @ManyToOne
    @JoinColumn(name = "product_id", updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private ColorProduct color;

    @Column(name = "product_img")
    private String productImg;

    @Column(name = "import_price", nullable = false)
    private BigDecimal importPrice;

    @Column(name = "sell_price", nullable = false)
    private BigDecimal sellPrice;


    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

}
