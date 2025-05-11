package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Order;
import com.tuyenvp.spring_boot_app.Model.OrderDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, Long> {
    boolean existsByOrderUserIdAndProductVariant_VariantId(Integer userId, Integer variantId);

    List<OrderDetail> findByOrder(Order order);

    @Query("SELECT od FROM OrderDetail od WHERE od.order.orderId = :orderId")
    List<OrderDetail> findByOrderId(@Param("orderId") Long orderId);

}
