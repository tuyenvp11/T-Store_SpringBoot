package com.tuyenvp.spring_boot_app.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class VNPayConfig {

    public static final String vnp_TmnCode = "T7TUM6X8";
    public static final String vnp_HashSecret = "Q9LUCIBADM27KRLJA0GZ5K2KE18QV1J9";
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String vnp_ReturnUrl = "http://localhost:8080/payment-return";


    public String createPaymentUrl(long amount, String orderInfo, String orderId, HttpServletRequest request) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "bill";

        String vnp_TxnRef = orderId;
        String vnp_IpAddr = request.getRemoteAddr();
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String name : fieldNames) {
            String value = vnp_Params.get(name);
            if (hashData.length() > 0) {
                hashData.append('&');
                query.append('&');
            }
            hashData.append(name).append('=').append(value);
            query.append(URLEncoder.encode(name, StandardCharsets.US_ASCII))
                    .append('=')
                    .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
        }

        String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return vnp_PayUrl + "?" + query;
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

}
