package com.tuyenvp.spring_boot_app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "brand")
    private String brand;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "product_descrip", length = 100000)
    private String productDescrip;

    @Column(name = "original_price", nullable = false)
    private BigDecimal originalPrice;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "warranty", length = 50)
    private String warranty;

    /*@Column(name = "is_active")
    private boolean is_active;*/

    /*@OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Specification specification;*/

    // quan he 1 nhieu voi bang product variant
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductVariant> variants;

    @OneToOne(cascade = CascadeType.ALL) // Khi lưu Product, Specification cũng sẽ được lưu
    @JoinColumn(name = "spec_id", referencedColumnName = "spec_id")
    private Specification specification;

    /*public void setSpecification(Specification specification) {
        this.specification = specification;
        specification.setProduct(this); // Thiết lập quan hệ
    }*/

    @JoinColumn(name = "category_id",insertable = false,updatable = false)
    @JsonBackReference(value = "categorys-products")
    @ManyToOne
    private Category category;


}
