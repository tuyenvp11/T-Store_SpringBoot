package com.tuyenvp.spring_boot_app.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_dtls")
public class UserDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phone;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String gender;

    private String password;

    /*private String profileImage;*/

    private String role;

    private Boolean isEnable;

    private Boolean accountNonLocked;

    private Integer failedAttempt;

    private Date lockTime;

    private String resetToken;

    public boolean isLoggedIn() {
        // Add your login logic here (e.g., session or token verification)
        return this.isLoggedIn();
    }


}
