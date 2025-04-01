package com.example.farmsim.service;

import com.example.farmsim.model.dto.HarvestDTO;
import com.example.farmsim.model.dto.SimulationDTO;
import com.example.farmsim.model.entity.*;
import com.example.farmsim.repository.AgentCommandRepo;
import com.example.farmsim.repository.CropRepo;
import com.example.farmsim.repository.EnvironmentRepo;
import com.example.farmsim.repository.SimulationRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.farmsim.repository.CropYieldHistoryRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class SimulationService {

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private HarvestService harvestService;

    @Autowired
    private AgentCommandRepo agentCommandRepo;

    @Autowired
    private CropYieldHistoryRepo cropYieldHistoryRepo;// 新增

    @Autowired
    private AgentService agentService;

    // 获取模拟
    public Simulation getSimulationById(String simulationId) {
        return simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
    }

    // 创建模拟
    // src/main/java/com/example/farmsim/service/SimulationService.java
    public String createSimulation(SimulationDTO dto) {
        Simulation simulation = new Simulation();
        simulation.setId("SIM-" + System.currentTimeMillis());
        simulation.setName(dto.getName());
        simulation.setStatus(SimulationStatus.RUNNING);
        simulationRepo.save(simulation); // 直接保存，不初始化农田
        return simulation.getId();
    }

    // 获取模拟信息
    public SimulationDTO getSimulation(String id) {
        Simulation simulation = simulationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + id));
        SimulationDTO dto = new SimulationDTO();
        dto.setId(simulation.getId());
        dto.setName(simulation.getName());
        dto.setDescription(simulation.getDescription());
        return dto;
    }

    // 结束模拟并计算收成
    public void endSimulation(String simulationId) {
        // 1. 更新模拟状态为“已结束”
        Simulation simulation = simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
        simulation.setStatus(SimulationStatus.STOPPED);
        simulationRepo.save(simulation);

        // 2. 计算收成数据
        calculateHarvest(simulationId);

        boolean expertModeEnabled = simulation.getAgents().stream()
                .anyMatch(agent -> "AGRONOMIST".equals(agent.getRoleType()));
        harvestService.calculateRevenue(simulationId, expertModeEnabled);


        // 3. 记录日志
        System.out.println("模拟已结束，收成数据已计算并保存。");
    }

    // 计算收成
    private void calculateHarvest(String simulationId) {
        // 1. 处理 Agent 指令（保留）
        List<AgentCommand> commands = agentCommandRepo.findBySimulationId(simulationId);
        applyCommandsToEnvironmentAndCrops(commands, simulationId);

        // 2. 获取作物和环境数据（保留）
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);
        Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);

        // 3. 计算并保存收成（保留）
        for (Crop crop : crops) {
            double yield = calculateCropYield(crop, latestEnv);
            double quality = calculateCropQuality(crop, latestEnv);

            HarvestDTO harvestDto = new HarvestDTO();
            harvestDto.setCropName(crop.getCropName());
            harvestDto.setYield(yield);
            harvestDto.setQuality(quality);
            harvestDto.setHarvestTime(LocalDateTime.now());
            harvestDto.setSimulationId(simulationId);
            harvestService.saveHarvest(harvestDto);
        }
    }

    // 处理 Agent 指令并影响环境和农作物
    public void applyCommandsToEnvironmentAndCrops(List<AgentCommand> commands, String simulationId) {
        Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);

        // 保留指令对环境和作物的影响逻辑
        for (AgentCommand command : commands) {
            try {
                String action = command.getAction();
                JsonNode params = new ObjectMapper().readTree(command.getParameters());

                switch (action) {
                    case "irrigate":
                        latestEnv.setPrecipitation(latestEnv.getPrecipitation() + params.path("waterAmount").asDouble());
                        break;
                    case "fertilize":
                        latestEnv.setSoilFertility(latestEnv.getSoilFertility() + params.path("amount").asDouble());
                        break;
                    case "plant":
                        String cropId = params.path("target").asText();
                        crops.stream()
                                .filter(c -> c.getCropId().equals(cropId))
                                .findFirst()
                                .ifPresent(crop -> crop.setGrowthRate(crop.getGrowthRate() * 1.1));
                        break;
                }
            } catch (Exception e) {
                System.err.println("处理指令失败: " + e.getMessage());
            }
        }

        // 保存更新后的数据
        environmentRepo.save(latestEnv);
        cropRepo.saveAll(crops);
    }

    private void saveCropYieldHistory(String cropId, double yield, String simulationId) {
        CropYieldHistory history = new CropYieldHistory();
        history.setCropId(cropId);
        history.setYield(yield);
        history.setTimestamp(LocalDateTime.now());

        Simulation simulation = simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
        history.setSimulation(simulation);

        cropYieldHistoryRepo.save(history);
    }

    // 计算作物产量
    private double calculateCropYield(Crop crop, Environment env) {
        return crop.getGrowthRate() *
                (1 + env.getTemperature() * crop.getTempWeight()) *
                (1 + env.getSoilFertility() * crop.getSoilWeight()) *
                (1 + env.getPrecipitation() * crop.getWaterWeight());
    }

    // 计算作物质量
    private double calculateCropQuality(Crop crop, Environment env) {
        return crop.getGrowthRate() *
                (1 + env.getSoilFertility() * crop.getSoilWeight()) *
                (1 + env.getPrecipitation() * crop.getWaterWeight());
    }
}