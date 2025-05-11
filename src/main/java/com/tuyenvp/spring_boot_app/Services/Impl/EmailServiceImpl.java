package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Order;
import com.tuyenvp.spring_boot_app.Model.OrderDetail;
import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Util.FormatUtil;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FormatUtil formatUtils;

    @Autowired
    private DbConnect DbConnect;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tuyenpham191103@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    /*public Boolean sendMailForOrder(Order order, String status) throws Exception {
        String msg = "<p>Xin chào <b>" + order.getAddress().getReceiverName() + "</b>,</p>"
                + "<p>Đơn hàng của bạn đã <b>" + status + "</b>.</p>"
                + "<p><b>Chi tiết đơn hàng:</b></p>"
                + "<ul>";

        if (order.getOrderDetail() != null) {
            for (OrderDetail detail : order.getOrderDetail()) {
                msg += "<li>"
                        + "Sản phẩm: " + detail.getProductVariant().getProduct().getProductName() + "<br>"
                        + "Phân loại: " + detail.getProductVariant().getProduct().getBrand() + " / " + detail.getProductVariant().getColor() + "<br>"
                        + "Số lượng: " + detail.getQuantity() + "<br>"
                        + "Giá: " + formatUtils.formatCurrency(detail.getSellPrice().doubleValue())
                        + "</li><br>";
            }
        } else {
            msg += "<li>Không có sản phẩm nào trong đơn hàng.</li>";
        }

        msg += "</ul>"
                + "<p>Phương thức thanh toán: " + order.getPaymentType() + "</p>"
                + "<p>Tổng tiền: <b>" + formatUtils.formatCurrency(order.getTotalPrice().doubleValue()) + "</b></p>"
                + "<p><a href='http://localhost:8080/user/order-detail/" + order.getOrderId() + "'>Xem chi tiết đơn hàng</a></p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("tuyenpham191103@gmail.com", "T-store");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject("Thông báo đơn hàng từ T-store");
        helper.setText(msg, true); // true = gửi HTML

        mailSender.send(message);
        return true;
    }*/

    /*public Boolean sendMailForOrder(Order order, String status) throws Exception {
        if (order == null || order.getOrderDetail() == null) return false;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("tuyenpham191103@gmail.com", "T-store");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject("Xác nhận đơn hàng #" + order.getOrderId());

        StringBuilder productList = new StringBuilder();
        for (OrderDetail detail : order.getOrderDetail()) {
            productList.append("<tr>")
                    .append("<td>").append(detail.getProductVariant().getProduct().getProductName()).append("</td>")
                    .append("<td>").append(detail.getProductVariant().getColor()).append(" / ")
                    *//*.append(detail.getProductVariant().getCapacity()).append("</td>")*//*
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(FormatUtil.formatCurrency(detail.getSellPrice().doubleValue())).append("</td>")
                    .append("</tr>");
        }

        String msg = """
            <p>Xin chào <b>%s</b>,</p>
            <p>Đơn hàng <b>#%d</b> của bạn đã được <b>%s</b>.</p>
            <p><b>Ngày đặt:</b> %s</p>
            <p><b>Phương thức thanh toán:</b> %s</p>
            <br>
            <table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse;'>
                <thead>
                    <tr>
                        <th>Sản phẩm</th>
                        <th>Phân loại</th>
                        <th>Số lượng</th>
                        <th>Giá</th>
                    </tr>
                </thead>
                <tbody>
                    %s
                </tbody>
            </table>
            <br>
            <p><b>Tổng tiền:</b> %s</p>
            <p>Cảm ơn bạn đã mua hàng tại <b>T-store</b>.</p>
        """.formatted(
                order.getAddress().getReceiverName(),
                order.getOrderId(),
                status,
                order.getOrderDate(),
                order.getPaymentType(),
                productList.toString(),
                FormatUtil.formatCurrency(order.getTotalPrice().doubleValue())
        );

        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }*/

    public Boolean sendMailForOrder(Order order, String status) throws Exception {
        StringBuilder msg = new StringBuilder();

        msg.append("<p>Xin chào ").append(order.getAddress().getReceiverName()).append(",</p>");
        msg.append("<p>Cảm ơn bạn đã đặt hàng. Trạng thái hiện tại của đơn hàng: <b>").append(status).append("</b>.</p>");
        msg.append("<p><b>Chi tiết đơn hàng:</b></p>");
        msg.append("<table border='1' cellpadding='5' cellspacing='0'>")
                .append("<tr><th>Sản phẩm</th><th>Thương hiệu</th><th>Màu</th><th>Số lượng</th><th>Giá</th></tr>");

        List<OrderDetail> details = DbConnect.orderDetailRepo.findByOrder(order); // đảm bảo repo có phương thức này

        for (OrderDetail detail : details) {
            ProductVariant variant = detail.getProductVariant();
            msg.append("<tr>")
                    .append("<td>").append(variant.getProduct().getProductName()).append("</td>")
                    .append("<td>").append(variant.getProduct().getBrand()).append("</td>")
                    .append("<td>").append(detail.getProductVariant().getColor().getColorName()).append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(formatUtils.formatCurrency(detail.getSellPrice().doubleValue())).append("</td>")
                    .append("</tr>");
        }

        msg.append("</table>");
        msg.append("<p>Phương thức thanh toán: ").append(order.getPaymentType()).append("</p>");
        msg.append("<p>Tổng tiền: ").append(formatUtils.formatCurrency(order.getTotalPrice().doubleValue())).append("</p>");
        msg.append("<p>Trân trọng,<br/>T-store</p>");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tuyenpham191103@gmail.com", "T-store");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject("Thông báo đơn hàng #" + order.getOrderId());
        helper.setText(msg.toString(), true);

        mailSender.send(message);
        return true;
    }



}
