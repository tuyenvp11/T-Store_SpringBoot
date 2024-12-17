package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.*;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.OrderService;
import com.tuyenvp.spring_boot_app.Util.CommonUtil;
import com.tuyenvp.spring_boot_app.Util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DbConnect DbConnect;

    @Autowired
    private CommonUtil commonUtil;


    // Lưu đơn hàng
    @Override
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {
        List<Cart> carts = DbConnect.cartRepo.findByUserId(userid);

        for (Cart cart : carts) {
            ProductOrder order = new ProductOrder();

//            String orderId = UUID.randomUUID().toString().substring(0, 8);
//            order.setOrderId(orderId);
//            Tạo mã đơn hàng sử dụng thời gian
//            String orderId = "ĐH" + System.currentTimeMillis();
//            order.setOrderId(orderId);
            order.setOrderDate(LocalDateTime.now());

            order.setProduct(cart.getProduct());
            order.setPrice(new BigDecimal(Float.toString(cart.getProduct().getProduct_price())));

            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());

            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setPhone(orderRequest.getPhone());
            address.setProvince(orderRequest.getProvince());
            address.setDistrict(orderRequest.getDistrict());
            address.setCommune(orderRequest.getCommune());
            address.setAddress(orderRequest.getAddress());

            order.setOrderAddress(address);

            ProductOrder saveOrder = DbConnect.orderRepo.save(order);
            commonUtil.sendMailForProductOrder(saveOrder, "Thành công");
        }
    }

    // Lấy danh sách đơn hàng theo User
    @Override
    public List<ProductOrder> getOrdersByUser(Integer userId) {
        List<ProductOrder> orders = DbConnect.orderRepo.findByUserId(userId);

        for (ProductOrder order : orders) {
            if (order.getPrice() == null) {
                order.setPrice(BigDecimal.ZERO);
            }
            if (order.getQuantity() == null) {
                order.setQuantity(0);
            }
        }
        return orders;
    }

    // Cập nhật trạng thái cho đơn hàng
    @Override
    public ProductOrder updateOrderStatus(Integer id, String status) {
        Optional<ProductOrder> findById = DbConnect.orderRepo.findById(id);
        if (findById.isPresent()) {
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            ProductOrder updateOrder = DbConnect.orderRepo.save(productOrder);
            return updateOrder;
        }
        return null;
    }

    @Override
    public List<ProductOrder> searchProductOrder(String keyword) {
        return DbConnect.orderRepo.searchProductOrder(keyword);
    }

    @Override
    public Page<ProductOrder> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        return DbConnect.orderRepo.findAll(pageable);
    }

    @Override
    public Page<ProductOrder> searchProductOrder(String keyword, Integer pageNo) {
        List list = searchProductOrder(keyword);
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end =(int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                ? list.size()
                : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);

        return new PageImpl<ProductOrder>(list, pageable, searchProductOrder(keyword).size());
    }

    /*@Override
    public ProductOrder getOrdersByOrderId(String orderId) {
        return DbConnect.orderRepo.findByOrderId(orderId);
    }*/

    @Override
    public long getTotalOrders() {
        return DbConnect.orderRepo.count();
    }

    @Override
    public double getTotalRevenue() {
        return DbConnect.orderRepo.findAll()
                .stream()
                .mapToDouble(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())).doubleValue())
                .sum();
    }



}
