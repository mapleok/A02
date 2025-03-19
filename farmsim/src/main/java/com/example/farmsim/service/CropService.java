package com.example.farmsim.service;

import com.example.farmsim.model.dto.CropDTO;
import com.example.farmsim.model.entity.Crop;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.repository.CropRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.farmsim.repository.SimulationRepo;

import java.util.UUID;

@Service
public class CropService {
    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    public String createCrop(CropDTO dto) {
        Crop crop = new Crop();
        crop.setCropId(UUID.randomUUID().toString());
        crop.setCropName(dto.getCropName());
        crop.setGrowthRate(dto.getGrowthRate());
        crop.setTempWeight(dto.getTempWeight());
        crop.setSoilWeight(dto.getSoilWeight());
        crop.setWaterWeight(dto.getWaterWeight());

        // 关联模拟
        Simulation simulation = simulationRepo.findById(dto.getSimulationId())
                .orElseThrow(() -> new RuntimeException("模拟ID " + dto.getSimulationId() + " 不存在"));
        crop.setSimulation(simulation);

        cropRepo.save(crop);
        return crop.getCropId();
    }
}