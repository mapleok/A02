package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


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

    @Setter
    private boolean completed;

    public boolean getCompleted() {
        return completed;
    }

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation; // 关联的模拟
}