package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OptimizationAction {
    @Id
    @GeneratedValue
    private Long id;
    private String simulationId;
    private String actionType; // 操作类型（如 "irrigation", "fertilization"）
    private String target;     // 操作目标（如作物ID）
    private double amount;     // 操作量（如灌溉量、施肥量）
    private LocalDateTime timestamp;
}