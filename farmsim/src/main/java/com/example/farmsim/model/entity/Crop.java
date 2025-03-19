package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private com.example.farmsim.model.entity.Simulation simulation;

    // Getters and Setters
}