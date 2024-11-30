package com.tuyenvp.spring_boot_app.Repository;

import java.util.List;
import java.util.Optional;

import com.tuyenvp.spring_boot_app.Model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<UserDtls, Integer> {

    public UserDtls findByEmail(String email);

    Optional <UserDtls> findByName(String name);

    public List<UserDtls> findByRole(String role);

    public UserDtls findByResetToken(String token);

    public Boolean existsByEmail(String email);
}