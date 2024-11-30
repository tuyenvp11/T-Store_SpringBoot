package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.product.product_id = :productId AND c.user.id = :userId")
    Cart findByProductAndUser(@Param("productId") int productId, @Param("userId") int userId);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user.id = :userId")
    Integer countByUserId(@Param("userId") int userId);

    public List<Cart> findByUserId(Integer userId);
}
