package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.*;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.CartService;
import com.tuyenvp.spring_boot_app.Services.DiscountService;
import com.tuyenvp.spring_boot_app.Services.OrderService;
import com.tuyenvp.spring_boot_app.Util.CommonUtil;
import com.tuyenvp.spring_boot_app.Util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DbConnect DbConnect;

    @Autowired
    private CartService cartService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Override
    @Transactional
    public Long placeOrder(String paymentMethod, String email, HttpSession session) {
        // 1. Lấy user
        UserDtls user = DbConnect.userRepo.findByEmail(email);

        // 2. Lấy địa chỉ mặc định
        Address defaultAddress = DbConnect.addressRepo.findByUserIdAndIsDefault(user.getId(), true);

        // 3. Lấy giỏ hàng
        List<Cart> cartItems = cartService.getCartByUser(user.getId());

        // Tính tổng tạm tính (subtotal)
        BigDecimal totalPrice = cartItems.stream()
                .map(cart -> cart.getProductVariant().getSellPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Lấy mã giảm giá từ session nếu có
        Discount discount = (Discount) session.getAttribute("appliedDiscount");
        BigDecimal discountValue = (discount != null) ? discount.getDiscountValue() : BigDecimal.ZERO;
        BigDecimal totalOrderPrice = totalPrice.subtract(discountValue);

        // 7. Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.IN_PROGRESS.getName());
        order.setPaymentType(paymentMethod);
        order.setTotalPrice(totalOrderPrice);
        order.setAddress(defaultAddress);
        order.setDiscount(discount);

        DbConnect.orderRepo.save(order);

        // 8. Tạo chi tiết đơn hàng
        for (Cart cartItem : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProductVariant(cartItem.getProductVariant());
            detail.setQuantity(cartItem.getQuantity());
            detail.setSellPrice(cartItem.getProductVariant().getSellPrice());

            DbConnect.orderDetailRepo.save(detail);
        }

        // 9. Xóa giỏ hàng
        DbConnect.cartRepo.deleteAll(cartItems);

        // 10. Xóa mã giảm giá khỏi session
        session.removeAttribute("discountId");

        // 11. Gửi email thông báo đơn hàng
        try {
            emailServiceImpl.sendMailForOrder(order, order.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return order.getOrderId();
    }


    // Lấy danh sách đơn hàng theo User
    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        List<Order> orders = DbConnect.orderRepo.findByUserId(userId);
        return orders;
    }

    @Override
    public Order updateOrderStatus(Integer id, String status) {
        Optional<Order> findById = DbConnect.orderRepo.findById(id);
        if (findById.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + id);
        }

        Order order = findById.get();

        // Nếu cập nhật sang DELIVERED và chưa trừ kho
        if ("Đã giao hàng".equalsIgnoreCase(status) && !order.isStockDeducted()) {
            List<OrderDetail> orderDetails = DbConnect.orderDetailRepo.findByOrder(order);
            for (OrderDetail detail : orderDetails) {
                ProductVariant variant = detail.getProductVariant();
                int currentStock = variant.getStockQuantity();
                int qty = detail.getQuantity();

                if (currentStock < qty) {
                    throw new RuntimeException("Không đủ hàng trong kho cho biến thể: " + variant.getVariantId());
                }

                variant.setStockQuantity(currentStock - qty);
                DbConnect.productVariantRepo.save(variant);
            }

            order.setStockDeducted(true); // đánh dấu đã trừ kho
        }

        order.setStatus(status);
        return DbConnect.orderRepo.save(order);
    }


    @Override
    public List<Order> searchOrder(String keyword) {
        return DbConnect.orderRepo.searchOrder(keyword);
    }

    @Override
    public Page<Order> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 10);
        return DbConnect.orderRepo.findAll(pageable);
    }

    @Override
    public Page<Order> searchOrder(String keyword, Integer pageNo) {
        List list = searchOrder(keyword);
        Pageable pageable = PageRequest.of(pageNo-1, 10);
        Integer start = (int) pageable.getOffset();
        Integer end =(int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                ? list.size()
                : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);

        return new PageImpl<Order>(list, pageable, searchOrder(keyword).size());
    }

    @Override
    public Page<Order> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        return null;
    }

    /*@Override
    public Order getOrdersByOrderId(String orderId) {
        return DbConnect.orderRepo.findByOrderId(orderId);
    }*/


    // phuong thuc dem tong so luong don hang
    @Override
    public long getTotalOrders() {
        return DbConnect.orderRepo.count();
    }


    @Override
    public void createOrderAfterPayment(String orderId) {
        // Thêm logic tạo đơn hàng sau khi thanh toán thành công
        // Gồm lưu đơn, gửi email, cập nhật giỏ hàng, v.v
        System.out.println("Đã tạo đơn hàng mã: " + orderId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return DbConnect.orderRepo.findById(orderId.intValue()).orElse(null);
    }

    @Override
    public List<OrderDetail> getOrderDetails(Long orderId) {
        return DbConnect.orderDetailRepo.findByOrderId(orderId);
    }

    @Override
    public Order findByOrderId(Long orderId) {
        return DbConnect.orderRepo.findByOrderId(orderId).orElse(null);
    }

    /*@Override
    public double getTotalRevenue() {
        return DbConnect.orderRepo.findAll()
                .stream()
                .mapToDouble(product -> product.getSellPrice().multiply(BigDecimal.valueOf(product.getQuantity())).doubleValue())
                .sum();

    }*/



}
