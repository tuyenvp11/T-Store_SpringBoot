package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Discount;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DbConnect DbConnect;

    @Override
    public List<Discount> getAllDiscount() {
        return DbConnect.discountRepo.findAll();
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal totalOrderPrice) {
        // Tìm mã giảm giá phù hợp với tổng giá trị đơn hàng
        Discount discount = DbConnect.discountRepo.findByMinAmountLessThanEqualAndMaxAmountGreaterThanEqual(totalOrderPrice, totalOrderPrice);

        if (discount != null) {
            return discount.getDiscountValue();  // Trả về giá trị giảm giá
        }

        return BigDecimal.ZERO;  // Không có mã giảm giá nếu không khớp với bất kỳ khoảng giá nào
    }

    @Override
    public Discount findById(Long discountId) {
        return DbConnect.discountRepo.findById(discountId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá với id: " + discountId));
    }

    @Override
    public List<Discount> ListDiscount() {
        return DbConnect.discountRepo.findAll();
    }

    // Thêm mã giảm giá
    @Override
    public String addDiscount(Discount add_discount) {
        //Optional<Discount> existingDiscount = DbConnect.discountRepo.findByCode(add_discount.getCode());

        /*if (existingDiscount.isPresent()) {
            return "Mã giảm gia đã tồn tại!";
        }*/
        DbConnect.discountRepo.save(add_discount);
        return "Thêm danh mục thành công!";
    }

    // Cập nhật mã giảm giá
    @Override
    public Discount updateDiscount(Discount edit_discount) {
        Optional<Discount> discount = DbConnect.discountRepo.findById(edit_discount.getDiscountId());
        if (discount.isEmpty()) {
            return null;
        }
        Discount update_discount = discount.get();
        // cập nhật tên danh mục
        update_discount.setCode(edit_discount.getCode());
        update_discount.setDiscountValue(edit_discount.getDiscountValue());
        update_discount.setMinAmount(edit_discount.getMinAmount());
        update_discount.setMaxAmount(edit_discount.getMaxAmount());
        DbConnect.discountRepo.save(update_discount);
        return update_discount;
    }


    // Xóa mã giảm giá
    @Override
    public Discount deleteDiscount(Long discountId) {
        Optional<Discount> discount = DbConnect.discountRepo.findById(discountId);
        if(discount.isEmpty()) {
            return null;
        }
        DbConnect.discountRepo.deleteById(discountId);
        return discount.get();
    }

    @Override
    public Optional<Discount> findByDiscountId(Long discountId) {
        return DbConnect.discountRepo.findById(discountId);
    }
}
