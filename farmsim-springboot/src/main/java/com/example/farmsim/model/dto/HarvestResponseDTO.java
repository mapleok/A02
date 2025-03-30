package com.example.farmsim.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HarvestResponseDTO {
    private Long id;
    private String cropName;
    private double yield;
    private double quality;
    private LocalDateTime harvestTime;
    private String simulationId; // 只返回模拟 ID，而不是整个模拟对象
}