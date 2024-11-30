package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="colorproducts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColorProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="color_id")
    private int color_id;

    @Column(name="color_name")
    private String color_name;
}
