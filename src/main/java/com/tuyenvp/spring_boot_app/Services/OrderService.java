package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.OrderRequest;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.ProductOrder;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public ProductOrder updateOrderStatus(Integer id, String status);

    //public List<ProductOrder> getAllOrders();

    public List<ProductOrder> searchProductOrder(String keyword);

    Page<ProductOrder> getAll(Integer pageNo);

    Page<ProductOrder> searchProductOrder(String keyword, Integer pageNo);

    //public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);

    public long getTotalOrders();

    public double getTotalRevenue();

}
