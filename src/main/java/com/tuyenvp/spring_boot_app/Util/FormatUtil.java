package com.tuyenvp.spring_boot_app.Util;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {
    public static String formatCurrency(double amount) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(vietnam);
        return formatter.format(amount).replace("₫", "đ");
    }

    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        ZonedDateTime vietnamTime = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(VIETNAM_ZONE);
        return vietnamTime.format(DATE_TIME_FORMATTER);
    }

    // date
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Định dạng chỉ ngày
    public String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }
}
