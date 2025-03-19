package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

// src/main/java/com/example/farmsim/model/entity/UserGoal.java
@Entity
@Getter
@Setter
public class UserGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cropType;       // 作物类型
    private double targetYield;    // 目标产量
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation; // 关联的模拟
}