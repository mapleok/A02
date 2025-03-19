package com.example.farmsim.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EnvironmentDTO {
    private Long id;
    private double temperature;
    private double soilFertility;
    private double precipitation;
    private String terrain;          // 地形
    private String climate;          // 气候
    private String agriculturalTechnology; // 农业技术
    private LocalDateTime timestamp;
    private String simulationId;
    // Getters and Setters
}