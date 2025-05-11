package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, Long> {
    Optional<DiscountRepo> findByDiscountId(Discount discount);
    Discount findByCode(String code);
    Discount findByMinAmountLessThanEqualAndMaxAmountGreaterThanEqual(BigDecimal minAmount, BigDecimal maxAmount);

    //Optional<Discount> findByCode(String code);

    //boolean existsByCode(String code); // Kiểm tra danh mục đã tồn tại chưa
}
