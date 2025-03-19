package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class HistoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logMessage;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private com.example.farmsim.model.entity.Simulation simulation;

    // Getters and Setters
}