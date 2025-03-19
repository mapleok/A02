package com.example.farmsim.repository;

import com.example.farmsim.model.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropRepo extends JpaRepository<Crop, String> {
    List<Crop> findBySimulationId(String simulationId);
}