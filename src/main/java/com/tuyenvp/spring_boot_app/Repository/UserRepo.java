package com.tuyenvp.spring_boot_app.Repository;

import java.util.List;

import com.tuyenvp.spring_boot_app.Model.UserDtls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserDtls, Integer> {

    public UserDtls findByEmail(String email);

    UserDtls findByName(String name);

    public List<UserDtls> findByRole(String role);

    public UserDtls findByResetToken(String token);

    public Boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM UserDtls u WHERE u.role = :role")
    long countByRole(@Param("role") String role);

    @Query("SELECT u FROM UserDtls u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(u.id AS string) LIKE CONCAT('%', :keyword, '%')")
    Page<UserDtls> searchUser(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM UserDtls u WHERE u.role = :role")
    Page<UserDtls> findByRolePage(@Param("role") String role, Pageable pageable);


}