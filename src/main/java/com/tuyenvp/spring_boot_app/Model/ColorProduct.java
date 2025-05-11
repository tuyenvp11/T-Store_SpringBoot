package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="color_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColorProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="color_id")
    private Integer colorId;

    @Column(name="color_name")
    private String colorName;

    /*@OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> productVariants = new ArrayList<>();*/


}
