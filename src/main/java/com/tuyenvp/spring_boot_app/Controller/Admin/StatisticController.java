package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Services.ChartService;
import com.tuyenvp.spring_boot_app.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class StatisticController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ChartService chartService;

    @GetMapping("/statistic")
    public String statistic(Model model) {
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());

        return "admin/statistic";
    }

    // Endpoint trả về biểu đồ dạng cột
    @GetMapping("/chartBar-revenue")
    public ResponseEntity<byte[]> getChart() {
        try {
            byte[] chartImage = chartService.generateBarChart();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok().headers(headers).body(chartImage);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    //Endpoint trả về biểu đồ dạng hình tròn
    /*@GetMapping("/chartPie-product")
    public ResponseEntity<byte[]> getChartPie() {

    }*/
}
