package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.ProductOrder;
import com.tuyenvp.spring_boot_app.Repository.OrderRepo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class ChartService {

    @Autowired
    private OrderRepo orderRepo;

    // Tạo biểu đồ dạng cột
    public byte[] generateBarChart() throws IOException {

        // Lấy dữ liệu từ database
        List<Object[]> revenueByMonth = orderRepo.getRevenueByMonth();

        // Khởi tạo dữ liệu doanh thu mặc định cho 12 tháng
        Map<Integer, Double> monthlyRevenue = new HashMap<>();
        for (int i = 1; i<12; i++){
           monthlyRevenue.put(i, 0.0);
        }

        // Cập nhật doanh thu lấy từ cơ sở dữ liệu
        for(Object[] row : revenueByMonth) {
            Integer month = (Integer) row[0];
            Double totalRevenue = ((Number) row[1]).doubleValue();
            monthlyRevenue.put(month, totalRevenue);
        }

        // Tạo dữ liệu biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int month = 1; month<=12; month++){
            dataset.addValue(monthlyRevenue.get(month), "Doanh thu", ""+month);
        }

        // Tạo biểu đồ
        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê doanh thu",      // Tiêu đề biểu đồ
                "Tháng",                   // Trục X
                "Doanh thu (VNĐ)",             // Trục Y
                dataset,                   // Dữ liệu
                PlotOrientation.VERTICAL,  // Hướng biểu đồ
                true,                      // Hiển thị chú thích
                true,                      // Hiển thị công cụ
                false                      // Không dùng URL
        );

        // Chỉnh sửa độ rộng của cột
        CategoryPlot plot = barChart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.2); // Điều chỉnh margin giữa các cột
        domainAxis.setLowerMargin(0.02);   // Margin trước cột đầu tiền
        domainAxis.setUpperMargin(0.02);   // Margin sau cột cuối cùng

        // Chuyển biểu đồ thành hình ảnh
        BufferedImage bufferedImage = barChart.createBufferedImage(800, 600);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    // Tạo biểu đồ hình tròn
    /*public byte[] generatePieChart() throws IOException {

    }*/
}
