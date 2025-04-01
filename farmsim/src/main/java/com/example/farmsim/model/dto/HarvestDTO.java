package com.example.farmsim.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HarvestDTO {
    private String cropName;       // 作物名称
    private double yield;         // 产量（单位：吨）
    private double quality;       // 质量评分（0-100）
    private LocalDateTime harvestTime; // 收获时间
    private String simulationId;  // 关联的模拟 ID
}