package com.tuyenvp.spring_boot_app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productvariants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "variant_id")
    private int variant_id;

    @Column(name = "product_id")
    private int product_id;

    @Column(name = "size_id")
    private int size_id;

    @Column(name = "color_id")
    private int color_id;

    @Column(name = "product_quantity")
    private int product_quantity;

    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    @JsonBackReference(value = "products-productvariants")
    @ManyToOne
    private Product product;

    @JoinColumn(name = "size_id",insertable = false,updatable = false)
    @JsonBackReference(value = "sizeproducts-productvariants")
    @ManyToOne
    private SizeProduct size;

    @JoinColumn(name = "color_id",insertable = false,updatable = false)
    @JsonBackReference(value = "colorproducts-productvariants")
    @ManyToOne
    private ColorProduct color;

}
