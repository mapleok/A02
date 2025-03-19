package com.example.farmsim.repository;

import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.model.entity.SimulationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationRepo extends JpaRepository<Simulation, String> {
    List<Simulation> findByStatus(SimulationStatus status);
}