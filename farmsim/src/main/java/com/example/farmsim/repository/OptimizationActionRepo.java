package com.example.farmsim.repository;

import com.example.farmsim.model.entity.OptimizationAction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptimizationActionRepo extends JpaRepository<OptimizationAction, Long> {
    List<OptimizationAction> findBySimulationId(String simulationId);
}