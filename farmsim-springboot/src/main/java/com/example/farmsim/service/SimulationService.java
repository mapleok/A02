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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// SimulationService.java
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
    public String createSimulation(SimulationDTO dto) {
        try {
            Simulation simulation = new Simulation();
            simulation.setId("SIM-" + System.currentTimeMillis());
            simulation.setName(dto.getName());
            simulation.setDescription(dto.getDescription());
            simulation.setStatus(SimulationStatus.STOPPED);

            // 设置模拟开始日期为当前日期
            simulation.setStartDate(LocalDate.now());

            simulationRepo.save(simulation);
            return simulation.getId();
        } catch (Exception e) {
            throw new RuntimeException("创建模拟失败: " + e.getMessage());
        }
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
        // 1. 处理 Agent 指令
        List<AgentCommand> commands = agentCommandRepo.findBySimulationId(simulationId);
        applyCommandsToEnvironmentAndCrops(commands, simulationId);

        // 2. 获取当前模拟的作物数据
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);

        // 3. 获取当前模拟的环境数据
        Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);

        // 4. 计算每种作物的收成
        for (Crop crop : crops) {
            double yield = calculateCropYield(crop, latestEnv); // 计算产量
            double quality = calculateCropQuality(crop, latestEnv); // 计算质量

            // 5. 保存收成数据
            HarvestDTO dto = new HarvestDTO();
            dto.setCropName(crop.getCropName());
            dto.setYield(yield);
            dto.setQuality(quality);
            dto.setHarvestTime(LocalDateTime.now());
            dto.setSimulationId(simulationId);
            harvestService.saveHarvest(dto);
        }
    }

    // 处理 Agent 指令并影响环境和农作物
    public void applyCommandsToEnvironmentAndCrops(List<AgentCommand> commands, String simulationId) {
        Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);

        for (Crop crop : crops) {
            double initialYield = calculateCropYield(crop, latestEnv);
            saveCropYieldHistory(crop.getCropId(), initialYield, simulationId);
        }

        for (AgentCommand command : commands) {
            try {
                String action = command.getAction();
                JsonNode params = new ObjectMapper().readTree(command.getParameters());

                switch (action) {
                    case "irrigate":
                        // 灌溉：增加降水量
                        double waterAmount = params.path("waterAmount").asDouble();
                        latestEnv.setPrecipitation(latestEnv.getPrecipitation() + waterAmount);
                        break;
                    case "fertilize":
                        // 施肥：提高土壤肥力
                        double fertilizer = params.path("amount").asDouble();
                        latestEnv.setSoilFertility(latestEnv.getSoilFertility() + fertilizer);
                        break;
                    case "plant":
                        // 种植：调整作物生长率
                        String cropId = params.path("target").asText();
                        Crop crop = crops.stream()
                                .filter(c -> c.getCropId().equals(cropId))
                                .findFirst()
                                .orElse(null);
                        if (crop != null) {
                            crop.setGrowthRate(crop.getGrowthRate() * 1.1); // 示例调整
                        }
                        break;
                }
            } catch (Exception e) {
                System.err.println("处理指令失败: " + e.getMessage());
            }
        }

        for (Crop crop : crops) {
            double updatedYield = calculateCropYield(crop, latestEnv);
            saveCropYieldHistory(crop.getCropId(), updatedYield, simulationId);
        }

        // 保存更新后的环境数据
        environmentRepo.save(latestEnv);
        agentService.triggerEnvironmentResponse(simulationId, latestEnv);
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