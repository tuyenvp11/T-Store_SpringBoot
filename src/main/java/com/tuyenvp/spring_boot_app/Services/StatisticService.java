package com.tuyenvp.spring_boot_app.Services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StatisticService {
    public Map<String, Double> getMonthlyRevenueMap();

    public Double getRevenueLastXMonths(int months);
    public Map<String, Double> getAllRevenuesByRange();

    public Map<String, Double> getMonthlyRevenue();
    public Map<String, Double> getQuarterlyRevenue();

    List<Object[]> getTopSellingProducts(int top);
    long getProductCount();
    long getOrderCount();
    long getUserCount();
}
