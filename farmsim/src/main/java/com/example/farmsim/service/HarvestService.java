package com.example.farmsim.service;

import com.example.farmsim.model.dto.HarvestDTO;
import com.example.farmsim.model.dto.HarvestResponseDTO;
import com.example.farmsim.model.entity.Harvest;
import com.example.farmsim.model.entity.RevenueRecord;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.repository.HarvestRepo;
import com.example.farmsim.repository.RevenueRecordRepo;
import com.example.farmsim.repository.SimulationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestService {

    @Autowired
    private HarvestRepo harvestRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private RevenueRecordRepo revenueRecordRepo;

    // 保存收成数据
    public void saveHarvest(HarvestDTO harvestDTO) {
        String simulationId = harvestDTO.getSimulationId();
        Simulation simulation = simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));

        Harvest harvest = new Harvest();
        harvest.setCropName(harvestDTO.getCropName());
        harvest.setYield(harvestDTO.getYield());
        harvest.setQuality(harvestDTO.getQuality());
        harvest.setHarvestTime(harvestDTO.getHarvestTime());
        harvest.setSimulation(simulation);

        harvestRepo.save(harvest);
    }

    public void calculateRevenue(String simulationId, boolean expertModeEnabled) {
        List<Harvest> harvests = harvestRepo.findBySimulationId(simulationId);
        double totalRevenue = harvests.stream().mapToDouble(h -> h.getYield() * h.getQuality()).sum();

        RevenueRecord record = new RevenueRecord();
        record.setSimulationId(simulationId);
        record.setRevenue(totalRevenue);
        record.setExpertModeEnabled(expertModeEnabled);
        record.setTimestamp(LocalDateTime.now());
        revenueRecordRepo.save(record);
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