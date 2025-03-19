// src/main/java/com/example/farmsim/model/entity/CropYieldHistory.java
package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CropYieldHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cropId;          // 农作物 ID
    private double yield;           // 产量
    private LocalDateTime timestamp; // 记录时间

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;  // 关联的模拟
}