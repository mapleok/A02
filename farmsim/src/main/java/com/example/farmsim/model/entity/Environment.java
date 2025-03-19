package com.example.farmsim.model.entity;

import com.example.farmsim.model.entity.Simulation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

// Environment.java
@Entity
@Getter
@Setter
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double temperature;      // 温度（单位：℃）
    private double soilFertility;    // 土壤肥力（存储为小数，例如 0.8 表示 80%）
    private double precipitation;    // 降水量（单位：mm）
    private String terrain;          // 地形（如平原、山地、丘陵等）
    private String climate;          // 气候（如温带季风气候、热带雨林气候等）
    private String agriculturalTechnology; // 农业技术（如传统农业、精准农业等）
    private LocalDateTime timestamp; // 时间戳

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;
}