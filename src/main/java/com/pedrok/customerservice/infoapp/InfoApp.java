package com.pedrok.customerservice.infoapp;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "info.app")
@Data
public class InfoApp {
    private String name;
    private String description;
    private String version;

    @Value("${app.showInfoApp:false}")
    private boolean showInfoApp;

    @Bean
    CommandLineRunner runner(InfoApp infoApp) {
        return args -> {
            if (showInfoApp) System.out.println(infoApp);
        };
    }
}
