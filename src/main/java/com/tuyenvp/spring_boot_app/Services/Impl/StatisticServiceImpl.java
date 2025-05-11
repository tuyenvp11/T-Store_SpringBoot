package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private DbConnect DbConnect;

    @Override
    public Map<String, Double> getMonthlyRevenueMap() {
        List<Object[]> rawData = DbConnect.orderRepo.getMonthlyRevenue();
        Map<String, Double> result = new LinkedHashMap<>(); // giữ đúng thứ tự tháng

        String[] monthNames = {
                "", "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        };

        for (Object[] row : rawData) {
            int month = (int) row[0];
            double revenue = ((Number) row[1]).doubleValue();
            result.put(monthNames[month], revenue);
        }

        return result;
    }

    @Override
    public Double getRevenueLastXMonths(int months) {
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(months);
        Double revenue = DbConnect.orderRepo.getRevenueFromDate(fromDate);
        return revenue != null ? revenue : 0.0;
    }

    public Map<String, Double> getAllRevenuesByRange() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("1 Tháng", getRevenueLastXMonths(1));
        map.put("3 Tháng", getRevenueLastXMonths(3));
        map.put("6 Tháng", getRevenueLastXMonths(6));
        map.put("1 Năm", getRevenueLastXMonths(12));
        return map;
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() {
        Map<String, Double> result = new LinkedHashMap<>();
        int year = Year.now().getValue();
        for (int month = 1; month <= 12; month++) {
            Double revenue = DbConnect.orderRepo.getRevenueInMonth(year, month);
            result.put("Tháng " + month, revenue != null ? revenue : 0.0);
        }
        return result;
    }

    @Override
    public Map<String, Double> getQuarterlyRevenue() {
        Map<String, Double> result = new LinkedHashMap<>();
        int year = Year.now().getValue();
        for (int q = 1; q <= 4; q++) {
            Double revenue = DbConnect.orderRepo.getRevenueInQuarter(year, q);
            result.put("Quý " + q, revenue != null ? revenue : 0.0);
        }
        return result;
    }

    @Override
    public List<Object[]> getTopSellingProducts(int top) {
        return DbConnect.orderRepo.getTopSellingProducts(PageRequest.of(0, top));
    }

    @Override
    public long getProductCount() {
        return DbConnect.productRepo.count();
    }

    @Override
    public long getOrderCount() {
        return DbConnect.orderRepo.count();
    }

    @Override
    public long getUserCount() {
        return DbConnect.userRepo.count();
    }


}
