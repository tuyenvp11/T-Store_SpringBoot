package com.tuyenvp.spring_boot_app.Util;

import com.tuyenvp.spring_boot_app.Model.Order;
import com.tuyenvp.spring_boot_app.Model.OrderDetail;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Component
public class CommonUtil {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private FormatUtil formatUtils;


    /*public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tuyenpham191103@gmail.com", "T-Store");
        helper.setTo(reciepentEmail);

        String content = "<p>Xin chào,</p>" + "<p>Bạn đã yêu cầu thay đổi mật khẩu.</p>"
                + "<p>Vui lòng chọn link này để thay đổi mật khẩu:</p>" + "<p><a href=\"" + url
                + "\">Thay đổi mật khẩu</a></p>";
        helper.setSubject("Đổi mật khẩu");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }*/

    /*public static String generateUrl(HttpServletRequest request) {

        // http://localhost:8088/forgot-password
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }*/

    //String msg=null;;

    /*public Boolean sendMailForOrder(Order order, String status) throws Exception {

        StringBuilder msgBuilder = new StringBuilder();

        msgBuilder.append("<p>Xin chào <b>")
                .append(order.getAddress().getReceiverName())
                .append("</b>,</p>");

        msgBuilder.append("<p>Cảm ơn bạn đã <b>")
                .append(status)
                .append("</b> đơn hàng tại T-store.</p>");

        msgBuilder.append("<p><b>Chi tiết đơn hàng:</b></p>")
                .append("<table style='border-collapse: collapse; width: 100%;'>")
                .append("<thead>")
                .append("<tr>")
                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Tên sản phẩm</th>")
                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Danh mục</th>")
                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Số lượng</th>")
                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Giá</th>")
                .append("</tr>")
                .append("</thead><tbody>");

        for (OrderDetail detail : order.getOrderDetail()) {
            String productName = detail.getProductVariant().getProduct().getProductName();
            String categoryName = detail.getProductVariant().getProduct().getCategory().getCategoryName();
            int quantity = detail.getQuantity();
            String price = formatUtils.formatCurrency(detail.getSellPrice().doubleValue());

            msgBuilder.append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(productName).append("</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(categoryName).append("</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(quantity).append("</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(price).append("</td>")
                    .append("</tr>");
        }

        msgBuilder.append("</tbody></table>");

        msgBuilder.append("<p>Phương thức thanh toán: <b>")
                .append(order.getPaymentType())
                .append("</b></p>");

        msgBuilder.append("<p>Tổng tiền: <b>")
                .append(formatUtils.formatCurrency(order.getTotalPrice().doubleValue()))
                .append("</b></p>");

        // Thêm link xem chi tiết đơn hàng
        String link = "http://localhost:8080/user/order-detail/" + order.getOrderId();
        msgBuilder.append("<p>Bạn có thể xem chi tiết đơn hàng tại đây: <a href='")
                .append(link)
                .append("'>Xem đơn hàng</a></p>");

        msgBuilder.append("<p>Trân trọng,<br>T-store</p>");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("tuyenpham191103@gmail.com", "T-store");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject("Thông báo trạng thái đơn hàng #" + order.getOrderId());
        helper.setText(msgBuilder.toString(), true);

        mailSender.send(message);
        return true;
    }*/


    public UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }
}
