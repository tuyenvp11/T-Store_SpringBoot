package com.tuyenvp.spring_boot_app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "specification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_id")
    private Integer specId;

    /*@OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;*/

    @Column(name = "screen_size", length = 50)
    private String screenSize;

    @Column(name = "screen_type", length = 50)
    private String screenType;

    @Column(name = "resolution", length = 100)
    private String resolution;

    @Column(name = "chipset", length = 100)
    private String chipset;

    @Column(name = "ram", length = 50)
    private String ram;

    @Column(name = "storage", length = 50)
    private String storage;

    @Column(name = "battery", length = 50)
    private String battery;

    @Column(name = "charging", length = 50)
    private String charging;

    @Column(name = "os", length = 50)
    private String os;

    @Column(name = "camera_main", length = 255)
    private String cameraMain;

    @Column(name = "camera_front", length = 100)
    private String cameraFront;

    @Column(name = "weight", length = 50)
    private String weight;


}
