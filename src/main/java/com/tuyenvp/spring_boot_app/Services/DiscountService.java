package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Discount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public interface DiscountService {
    public List<Discount> getAllDiscount();

    public BigDecimal applyDiscount(BigDecimal totalOrderPrice);

    public Discount findById(Long discountId);

    List<Discount> ListDiscount();

    public String addDiscount(Discount add_discount);

    public Discount updateDiscount(Discount edit_discount);

    public Discount deleteDiscount(Long discountId);

    public Optional<Discount> findByDiscountId(Long discountId);
}
