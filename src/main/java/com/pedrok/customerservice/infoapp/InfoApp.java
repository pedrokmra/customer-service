package com.pedrok.customerservice.infoapp;

import lombok.Data;
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

    @Bean
    CommandLineRunner runner(InfoApp infoApp) {
        return args -> System.out.println(infoApp);
    }
}
