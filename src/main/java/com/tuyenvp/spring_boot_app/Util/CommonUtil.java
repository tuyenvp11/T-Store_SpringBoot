package com.tuyenvp.spring_boot_app.Util;

import com.tuyenvp.spring_boot_app.Model.ProductOrder;
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

    public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

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
    }

    public static String generateUrl(HttpServletRequest request) {

        // http://localhost:8088/forgot-password
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg=null;;

    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception
    {

        msg="<p>Xin chào [[name]],</p>"
                + "<p>Cảm ơn bạn đặt <b>[[orderStatus]]</b>.</p>"
                + "<p><b>Chi tiết sản phẩm:</b></p>"
                + "<p>Tên sản phẩm : [[productName]]</p>"
                + "<p>Danh mục : [[category]]</p>"
                + "<p>Số lượng : [[quantity]]</p>"
                + "<p>Giá : [[@formatUtils.formatCurrency(price)]]</p>"
                + "<p>Phương thức thanh toán : [[paymentType]]</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tuyenpham191103@gmail.com", "T-store");
        helper.setTo(order.getOrderAddress().getEmail());

        msg=msg.replace("[[name]]",order.getOrderAddress().getFirstName());
        msg=msg.replace("[[orderStatus]]",status);
        msg=msg.replace("[[productName]]", order.getProduct().getProduct_name());
        msg=msg.replace("[[category]]", String.valueOf(order.getProduct().getCategory_id()));
        msg=msg.replace("[[quantity]]", order.getQuantity().toString());
        msg=msg.replace("[[@formatUtils.formatCurrency(price)]]", order.getPrice().toString());
        msg=msg.replace("[[paymentType]]", order.getPaymentType());

        helper.setSubject("Trạng thái đơn hàng");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }

    public UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }
}
