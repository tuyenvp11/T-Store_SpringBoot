package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vendor_id")
    private int vendor_id;
    @Column(name="vendor_name")
    private String vendor_name;
    @Column(name="vendor_hotline")
    private int vendor_hotline;
    @Column(name="vendor_address")
    private String vendor_address;
}
