package com.tuyenvp.spring_boot_app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table (name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int product_id;

    @Column(name = "product_name")
    private String product_name;

    @Column(name = "category_id")
    private int category_id;

    @Column(name = "vendor_id")
    private int vendor_id;

    @Column(name = "product_img")
    private String product_img;

    @Column(name = "product_descrip", length = 100000)
    private String product_descrip;

    @Column(name = "product_price")
    private float product_price;

    @Column(name = "product_quantity")
    private int product_quantity;

    /*@Column(name = "is_active")
    private boolean is_active;*/

    @JoinColumn(name = "category_id",insertable = false,updatable = false)
    @JsonBackReference(value = "categorys-products")
    @ManyToOne
    private Category category;

    @JoinColumn(name = "vendor_id",insertable = false,updatable = false)
    @JsonBackReference(value = "vendors-products")
    @ManyToOne
    private Vendor vendor;

    // Getter Setter
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_descrip() {
        return product_descrip;
    }

    public void setProduct_descrip(String product_descrip) {
        this.product_descrip = product_descrip;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
