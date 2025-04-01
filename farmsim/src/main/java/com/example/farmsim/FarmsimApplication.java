package com.example.farmsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FarmsimApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmsimApplication.class, args);
    }

}
