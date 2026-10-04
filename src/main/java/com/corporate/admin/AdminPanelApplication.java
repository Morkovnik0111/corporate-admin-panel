package com.corporate.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableJpaAuditing
public class AdminPanelApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminPanelApplication.class, args);
    }
}