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
public class RevenueRecord {
    @Id
    @GeneratedValue
    private Long id;
    private String simulationId;
    private double revenue;
    private boolean expertModeEnabled;
    private LocalDateTime timestamp;
}