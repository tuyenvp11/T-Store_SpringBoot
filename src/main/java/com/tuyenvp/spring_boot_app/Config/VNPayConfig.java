package com.tuyenvp.spring_boot_app.Config;

import org.springframework.stereotype.Component;

@Component
public class VNPayConfig {
    public static final String vnp_TmnCode = "E7XBCXTP"; // Mã Terminal (Merchant)
    public static final String vnp_HashSecret = "15MLMJ5YC58SZ8Z28KO4NDM7X1FFPQZF"; // Secret Key
    public static final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"; // URL sandbox
    public static final String vnp_ReturnUrl = "http://localhost:8088/payment/vnpay-return"; // URL trả kết quả

    public static String getIpAddress() {
        return "127.0.0.1"; // Lấy IP người dùng (thay bằng mã lấy IP thực tế)
    }
}
