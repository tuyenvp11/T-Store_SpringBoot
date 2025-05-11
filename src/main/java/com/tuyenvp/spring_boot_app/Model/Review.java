package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    /*@ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;*/

    @ManyToOne
    @JoinColumn(name = "variant_id", referencedColumnName = "variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserDtls user;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating")
    private int rating;

    @Column(name = "reviewTime")
    private LocalDateTime reviewTime;
}
