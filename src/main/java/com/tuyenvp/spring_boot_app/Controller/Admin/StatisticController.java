package com.tuyenvp.spring_boot_app.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class StatisticController {
    @GetMapping("/statistic")
    public String statistic() {
        return "admin/statistic/statistic";
    }
}
