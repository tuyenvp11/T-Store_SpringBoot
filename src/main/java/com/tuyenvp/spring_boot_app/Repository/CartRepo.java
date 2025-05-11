package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Cart;
import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.productVariant.variantId = :variantId AND c.user.id = :userId")
    public Cart findByProductVariantAndUser(@Param("variantId") Integer variantId, @Param("userId") Integer userId);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user.id = :userId")
    Integer countByUserId(@Param("userId") int userId);

    public List<Cart> findByUserId(Integer userId);

    //@Query("SELECT c FROM Cart c WHERE c.user = :user AND c.productVariant = :productVariant")
    //Cart findByUserAndProductVariant(@Param("user") UserDtls user, @Param("variant") ProductVariant variant);


    public List<Cart> findByUser(UserDtls user);

    //Optional<Cart> findByUserAndProductVariant(UserDtls user, ProductVariant productVariant);
}
