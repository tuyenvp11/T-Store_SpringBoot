package com.tuyenvp.spring_boot_app.Controller;

import com.tuyenvp.spring_boot_app.Config.VNPayConfig;
import com.tuyenvp.spring_boot_app.Services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/")
public class VNPayController {
    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private OrderService orderService;

    /*@PostMapping("/pay-vnpay")
    public void payWithVNPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long totalAmount = 500000; // ví dụ: 500k (có thể lấy từ giỏ hàng)
        String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String orderInfo = "Thanh toán đơn hàng #" + orderId;

        String paymentUrl = vnPayConfig.createPaymentUrl(totalAmount, orderInfo, orderId, request);
        response.sendRedirect(paymentUrl);
    }

    @GetMapping("/payment-return")
    public String paymentReturn(HttpServletRequest request, Model model) {
        Map<String, String[]> fields = request.getParameterMap();
        Map<String, String> vnp_Params = new HashMap<>();

        for (Map.Entry<String, String[]> entry : fields.entrySet()) {
            vnp_Params.put(entry.getKey(), entry.getValue()[0]);
        }

        String vnp_SecureHash = vnp_Params.remove("vnp_SecureHash");
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String name : fieldNames) {
            String value = vnp_Params.get(name);
            if (hashData.length() > 0) hashData.append('&');
            hashData.append(name).append('=').append(value);
        }

        String secureHash = vnPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());

        if (secureHash.equals(vnp_SecureHash)) {
            String responseCode = vnp_Params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                // ✅ Thanh toán thành công → tạo đơn hàng
                orderService.createOrderAfterPayment(vnp_Params.get("vnp_TxnRef"));
                model.addAttribute("message", "Thanh toán thành công và đã tạo đơn hàng!");
            } else {
                model.addAttribute("message", "Thanh toán thất bại. Mã lỗi: " + responseCode);
            }
        } else {
            model.addAttribute("message", "Sai chữ ký! Giao dịch bị từ chối.");
        }

        return "payment-result"; // file .html hiển thị kết quả
    }*/
}
