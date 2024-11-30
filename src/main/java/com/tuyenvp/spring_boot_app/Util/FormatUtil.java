package com.tuyenvp.spring_boot_app.Util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtil {
    public static String formatCurrency(double amount) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(vietnam);
        return formatter.format(amount).replace("₫", "đ");
    }
}
