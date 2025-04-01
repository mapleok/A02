package com.example.farmsim.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CropDTO {
    private String cropId;
    private String cropName;
    private double growthRate = 1;
    private double tempWeight = 0.5;
    private double soilWeight = 0.5;
    private double waterWeight = 0.5;
    private String simulationId;
    private Float x;
    private Float z;
    private String cropType;


    // Getters and Setters
}