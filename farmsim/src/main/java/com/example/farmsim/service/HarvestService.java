package com.example.farmsim.service;

import com.example.farmsim.model.dto.HarvestDTO;
import com.example.farmsim.model.dto.HarvestResponseDTO;
import com.example.farmsim.model.entity.Harvest;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.repository.HarvestRepo;
import com.example.farmsim.repository.SimulationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestService {

    @Autowired
    private HarvestRepo harvestRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    // 保存收成数据
    public Long saveHarvest(HarvestDTO dto) {
        // 1. 查找关联的模拟
        Simulation simulation = simulationRepo.findById(dto.getSimulationId())
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + dto.getSimulationId()));

        // 2. 创建收成对象
        Harvest harvest = new Harvest();
        harvest.setCropName(dto.getCropName());
        harvest.setYield(dto.getYield());
        harvest.setQuality(dto.getQuality());
        harvest.setHarvestTime(dto.getHarvestTime());
        harvest.setSimulation(simulation);

        // 3. 保存数据
        harvestRepo.save(harvest);
        return harvest.getId();
    }

    // 查询指定模拟的收成数据
    public List<HarvestResponseDTO> getHarvestsBySimulationId(String simulationId) {
        List<Harvest> harvests = harvestRepo.findBySimulationId(simulationId);
        return harvests.stream()
                .map(this::convertToHarvestResponseDTO)
                .collect(Collectors.toList());
    }

    private HarvestResponseDTO convertToHarvestResponseDTO(Harvest harvest) {
        HarvestResponseDTO dto = new HarvestResponseDTO();
        dto.setId(harvest.getId());
        dto.setCropName(harvest.getCropName());
        dto.setYield(harvest.getYield());
        dto.setQuality(harvest.getQuality());
        dto.setHarvestTime(harvest.getHarvestTime());
        dto.setSimulationId(harvest.getSimulation().getId()); // 只返回模拟 ID
        return dto;
    }
}