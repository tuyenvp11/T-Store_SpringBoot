package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);

    Optional<Order> findByOrderId(Long orderId);

    // Truy vấn tìm kiếm đơn hàng
    @Query("SELECT o FROM Order o WHERE CAST(o.orderId AS string) LIKE %:keyword% OR o.user.name LIKE %:keyword%")
    List<Order> searchOrder(@Param("keyword") String keyword);

    // Truy vấn tính tổng doanh thu theo tháng
    /*@Query("SELECT MONTH(o.orderDate) AS month, SUM(o.totalPrice) AS totalRevenue " +
            "FROM Order o " +
            "GROUP BY MONTH(o.orderDate) ")
    List<Object[]> getRevenueByMonth();*/

    // doanh so theo thang
    @Query("SELECT MONTH(o.orderDate), SUM(o.totalPrice) " +
            "FROM Order o WHERE o.status = 'Đã giao hàng' " +
            "GROUP BY MONTH(o.orderDate) ORDER BY MONTH(o.orderDate)")
    List<Object[]> getMonthlyRevenue();

    // doanh so theo thoi gian nhat dinh
    @Query("SELECT SUM(o.totalPrice) FROM Order o " +
            "WHERE o.status = 'Đã giao hàng' AND o.orderDate >= :fromDate")
    Double getRevenueFromDate(@Param("fromDate") LocalDateTime fromDate);

    // doanh so theo quy
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = 'Đã giao hàng' AND YEAR(o.orderDate) = :year AND QUARTER(o.orderDate) = :quarter")
    Double getRevenueInQuarter(@Param("year") int year, @Param("quarter") int quarter);

    // doanh so theo thang
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = 'Đã giao hàng' AND YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    Double getRevenueInMonth(@Param("year") int year, @Param("month") int month);


    @Query("SELECT od.productVariant.product.productName, SUM(od.quantity) " +
            "FROM OrderDetail od JOIN od.order o WHERE o.status = 'DELIVERED' " +
            "GROUP BY od.productVariant.product.productName ORDER BY SUM(od.quantity) DESC")
    List<Object[]> getTopSellingProducts(Pageable pageable);
}
