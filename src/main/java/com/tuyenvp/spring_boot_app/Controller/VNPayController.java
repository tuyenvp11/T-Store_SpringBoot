package com.tuyenvp.spring_boot_app.Controller;

import com.tuyenvp.spring_boot_app.Config.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class VNPayController {
    @GetMapping("/payment/vn-pay")
    public RedirectView createPayment(HttpServletRequest request) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "billpayment";
        String vnp_TxnRef = UUID.randomUUID().toString(); // Mã giao dịch duy nhất
        String vnp_IpAddr = VNPayConfig.getIpAddress();

        // Thời gian tạo giao dịch
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(calendar.getTime());

        // Thời gian hết hạn
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());

        // Thông tin thanh toán
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(100000 * 100)); // Giá trị thanh toán (VND x 100)
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán hóa đơn #" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo URL
        List<String> fieldList = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldList);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldList) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, "UTF-8")).append('&');
                query.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, "UTF-8")).append('&');
            }
        }

        String queryUrl = query.substring(0, query.length() - 1);
        String vnp_SecureHash = VNPayConfig.vnp_HashSecret; // Tạo hash từ secret key
        hashData.deleteCharAt(hashData.length() - 1); // Xóa ký tự `&` cuối

        vnp_SecureHash = HmacSHA256(VNPayConfig.vnp_HashSecret, hashData.toString()); // Tạo mã hóa SHA256
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return new RedirectView(VNPayConfig.vnp_Url + "?" + queryUrl);
    }

    @GetMapping("/payment/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, Model model) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String vnp_ResponseCode = paramMap.get("vnp_ResponseCode")[0];
        if ("00".equals(vnp_ResponseCode)) {
            model.addAttribute("message", "Thanh toán thành công!");
        } else {
            model.addAttribute("message", "Thanh toán không thành công!");
        }
        return "user/payment-status";
    }

    // Hàm mã hóa SHA256
    private String HmacSHA256(String key, String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] result = mac.doFinal(data.getBytes());
            return Hex.encodeHexString(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
