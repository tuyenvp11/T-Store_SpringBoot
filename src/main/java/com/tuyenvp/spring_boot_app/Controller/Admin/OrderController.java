package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.Order;
import com.tuyenvp.spring_boot_app.Model.OrderDetail;
import com.tuyenvp.spring_boot_app.Services.Impl.EmailServiceImpl;
import com.tuyenvp.spring_boot_app.Services.OrderService;
import com.tuyenvp.spring_boot_app.Util.CommonUtil;
import com.tuyenvp.spring_boot_app.Util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private EmailServiceImpl emailService;

    @GetMapping("/order")
    public String getAllOrders(Model model,
                               @Param(value="keyword") String keyword,
                               @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
        Page<Order> orders = orderService.getAll(pageNo);
        if(keyword != null && !keyword.isEmpty()) {
            orders = orderService.searchOrder(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        }else{
            orders = orderService.getAll(pageNo);
        }
        model.addAttribute("totalPage", orders.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        model.addAttribute("ListOrder", orders.getContent());

        return "admin/order";
    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.getName();
            }
        }

        Order updateOrder = orderService.updateOrderStatus(id, status);

        try {
            emailService.sendMailForOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", "Trạng thái được cập nhật");
        } else {
            session.setAttribute("errorMsg", "Trạng thái không được cập nhật");
        }
        return "redirect:/admin/order";
    }

    /*public String updateOrderStatus(@RequestParam("orderId") Integer orderId,
                                    @RequestParam("status") String status,
                                    RedirectAttributes redirectAttributes) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + ex.getMessage());
        }

        return "redirect:/admin/order";
    }
*/

    @GetMapping("/order-detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        List<OrderDetail> orderDetails = orderService.getOrderDetails(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "admin/order_detail";  // tên file Thymeleaf
    }

}
