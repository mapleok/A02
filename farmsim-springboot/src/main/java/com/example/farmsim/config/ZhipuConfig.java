package com.example.farmsim.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "ai.zhipu")
public class ZhipuConfig {
    // Getters and Setters
    private String apiKey;
    private String endpoint;

}