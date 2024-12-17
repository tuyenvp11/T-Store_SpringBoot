package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByUserId(Integer userId);

    //ProductOrder findByOrderId(String orderId);

    // Truy vấn tìm kiếm đơn hàng
    @Query("SELECT o FROM ProductOrder o WHERE o.product.product_name LIKE %?1%")
    List<ProductOrder> searchProductOrder(String keyword);

    // Truy vấn tính tổng doanh thu theo tháng
    @Query("SELECT MONTH(o.orderDate) AS month, SUM(o.price * o.quantity) AS totalRevenue " +
            "FROM ProductOrder o " +
            "GROUP BY MONTH(o.orderDate) ")
    List<Object[]> getRevenueByMonth();
}
