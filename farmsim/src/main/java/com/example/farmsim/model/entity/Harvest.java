package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Harvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cropName;       // 作物名称
    private double yield;         // 产量（单位：吨）
    private double quality;       // 质量评分（0-100）
    private LocalDateTime harvestTime; // 收获时间

    @ManyToOne
    @JoinColumn(name = "simulation_id") // 关联模拟
    private Simulation simulation;
}