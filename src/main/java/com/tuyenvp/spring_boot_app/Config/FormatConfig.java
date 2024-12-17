package com.tuyenvp.spring_boot_app.Config;

import com.tuyenvp.spring_boot_app.Util.FormatUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormatConfig {
    @Bean
    public FormatUtil formatUtils() {
        return new FormatUtil();
    }
}
