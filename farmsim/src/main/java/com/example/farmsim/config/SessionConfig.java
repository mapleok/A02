package com.example.farmsim.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@Configuration
public class SessionConfig {
    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        };
    }
}