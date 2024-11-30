package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="sizeproducts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="size_id")
    private int size_id;

    @Column(name="size_name")
    private String size_name;
}
