package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Crop {
    @Id
    private String cropId;
    private String cropName;
    private double growthRate = 1;
    private double tempWeight = 0.5;
    private double soilWeight = 0.5;
    private double waterWeight = 0.5;

    private int maturityTime;
    @Column(nullable = false)
    private LocalDate plantingDate;
    private boolean isMature;
    private double growthProgress;

    private String cropType;

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private com.example.farmsim.model.entity.Simulation simulation;

}