package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "carts")
@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long cart_id;

    @ManyToOne
    @JoinColumn(name="variant_id", referencedColumnName = "variant_id", nullable=false)
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable=false)
    private UserDtls user;

    @Column(name="quantity")
    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private BigDecimal totalOrderPrice;

    // 🟢 Constructor không tham số (bắt buộc cho Hibernate)
    public Cart() {}

    // 🟢 Constructor đầy đủ
    public Cart(UserDtls user, ProductVariant productVariant, int quantity) {
        this.user = user;
        this.productVariant = productVariant;
        this.quantity = quantity;
    }

}
