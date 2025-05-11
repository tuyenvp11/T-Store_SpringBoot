package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserDtls user;

    private String type;

    @Column(name = "receiver_name")
    private String receiverName;

    private String receiverPhone;

    @Column(name = "full_address", columnDefinition = "TEXT")
    private String fullAddress;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
