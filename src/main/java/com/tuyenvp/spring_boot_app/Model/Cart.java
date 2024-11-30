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
@NoArgsConstructor
@AllArgsConstructor

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long cart_id;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id", nullable=false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable=false)
    private UserDtls user;

    @Column(name="quantity")
    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private BigDecimal totalOrderPrice;

    /*@JoinColumn(name = "user_id" , insertable = false, updatable = false)
    @JsonBackReference(value = "user_dtls-carts")
    @ManyToOne
    private UserDtls user_dtls;

    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonBackReference(value = "products-carts")
    @ManyToOne
    private Product product;*/

    public Long getCart_id() {
        return cart_id;
    }

    public void setCart_id(Long cart_id) {
        this.cart_id = cart_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UserDtls getUser_dtls() {
        return user;
    }

    public void setUser_dtls(UserDtls user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(BigDecimal totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }
}
