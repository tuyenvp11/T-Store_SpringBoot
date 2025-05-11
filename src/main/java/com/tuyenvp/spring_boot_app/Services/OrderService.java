package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.OrderDetail;
import com.tuyenvp.spring_boot_app.Model.Order;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public Long placeOrder(String paymentMethod, String email, HttpSession session);

    public List<Order> getOrdersByUser(Integer userId);

    public Order updateOrderStatus(Integer id, String status);

    public List<Order> searchOrder(String keyword);

    Page<Order> getAll(Integer pageNo);

    Page<Order> searchOrder(String keyword, Integer pageNo);

    public Page<Order> getAllOrdersPagination(Integer pageNo, Integer pageSize);

    public long getTotalOrders();

    public void createOrderAfterPayment(String orderId);

    public Order getOrderById(Long orderId);

    public List<OrderDetail> getOrderDetails(Long orderId);

    public Order findByOrderId(Long orderId);

}
