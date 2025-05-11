package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Address;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
    List<Address> findByUser(UserDtls user);

    // cap nhat dia chi mac dinh
    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = : userId")
    void clearDefaultForUser(@Param("userId") Integer userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isDefault = :isDefault")
    Address findByUserIdAndIsDefault(@Param("userId") Integer userId, @Param("isDefault") boolean isDefault);
}
