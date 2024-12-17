package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.ProductOrder;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;

    @GetMapping("/order")
    public String getAllOrders(Model model,
                               @Param(value="keyword") String keyword,
                               @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
        Page<ProductOrder> orders = orderService.getAll(pageNo);
        if(keyword != null && !keyword.isEmpty()) {
            orders = orderService.searchProductOrder(keyword, pageNo);
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

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);

        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
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


}
