package com.example.farmsim.websocket;

import com.example.farmsim.model.dto.HarvestDTO;
import com.example.farmsim.model.entity.*;
import com.example.farmsim.repository.*;
import com.example.farmsim.service.AgentService;
import com.example.farmsim.service.HarvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;


@Component
@Transactional
public class EnvScheduler {

    @Autowired
    private AgentCommandRepo agentCommandRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private HarvestService harvestService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private AgentRepo agentRepo;

    @Autowired
    private WebSocketHandler webSocketHandler;

    private double previousTemperature = Double.NaN; // 初始值为无效值
    private double previousSoilFertility = Double.NaN;
    private double previousPrecipitation = Double.NaN;


    // EnvScheduler.java
    private void resendCommandToUnity(AgentCommand command) {
        try {
            // 构建消息
            Message message = new Message();
            message.setType("agent-action");
            message.setSimulationId(command.getSimulation().getId());
            message.setData(Map.of(
                    "commandId", command.getId(),
                    "action", command.getAction(),
                    "parameters", command.getParameters()
            ));

            // 通过 WebSocket 发送消息
            webSocketHandler.broadcastMessage(message);
        } catch (Exception e) {
            System.err.println("重发指令失败: " + e.getMessage());
        }
    }

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


    private void harvestCrop(Crop crop) {
        Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(crop.getSimulation().getId());
        double yield = calculateCropYield(crop, env);
        double quality = calculateCropQuality(crop, env);
        HarvestDTO dto = new HarvestDTO();
        dto.setCropName(crop.getCropName());
        dto.setYield(yield);
        dto.setQuality(quality);
        dto.setHarvestTime(LocalDateTime.now());
        dto.setSimulationId(crop.getSimulation().getId());
        harvestService.saveHarvest(dto);
    }

    @Scheduled(fixedRate = 10000) // 每分钟执行一次
    public void optimizeResources() {
        List<Simulation> simulations = simulationRepo.findAll();
        for (Simulation sim : simulations) {
            // 检查是否有专家Agent
            List<Agent> experts = agentRepo.findBySimulationIdAndRoleType(sim.getId(), "AGRONOMIST");
            if (!experts.isEmpty()) {
                agentService.optimizeResourceAllocation(sim.getId());
            }
        }
    }

    @Scheduled(fixedRate = 10000) // 每分钟检测一次
    public void checkEnvironmentChanges() {
        List<Simulation> simulations = simulationRepo.findAll();
        for (Simulation sim : simulations) {
            Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(sim.getId());
            if (latestEnv != null && isEnvironmentChanged(latestEnv)) {
                agentService.optimizeResourceAllocation(sim.getId());
            }
        }
    }

    private void updateCropGrowth(Simulation sim) {
        List<Crop> crops = cropRepo.findBySimulationId(sim.getId());
        for (Crop crop : crops) {
            Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(sim.getId());
            long daysSincePlanting = ChronoUnit.DAYS.between(crop.getPlantingDate(), LocalDate.now());
            double growthProgress = (double) daysSincePlanting / 100; // 示例固定成熟时间
            crop.setGrowthProgress(Math.min(growthProgress, 1.0));
            if (growthProgress >= 1.0 && !crop.isMature()) {
                crop.setMature(true);
                harvestCrop(crop);
            }
            cropRepo.save(crop);
        }
    }

    @Scheduled(fixedRate = 15000)
    public void checkCommandStatus() {
        List<AgentCommand> pendingCommands = agentCommandRepo.findByStatus(CommandStatus.PENDING);
        pendingCommands.forEach(cmd -> {
            // 重发未确认指令
            resendCommandToUnity(cmd);
        });
    }

    @Scheduled(fixedRate = 15000)
    public void updateSeason() {
        List<Simulation> simulations = simulationRepo.findAll();
        for (Simulation sim : simulations) {
            String newSeason = calculateSeason(sim.getStartDate()); // 根据模拟开始日期计算季节
            sim.setSeason(newSeason);
            simulationRepo.save(sim);
        }
    }

    private String calculateSeason(LocalDate startDate) {
        // 计算模拟运行的天数
        long daysSinceStart = ChronoUnit.DAYS.between(startDate, LocalDate.now());

        // 根据天数计算当前季节
        int seasonIndex = (int) (daysSinceStart / 90) % 4; // 每 90 天为一个季节
        switch (seasonIndex) {
            case 0:
                return "spring";
            case 1:
                return "summer";
            case 2:
                return "autumn";
            case 3:
                return "winter";
            default:
                return "spring"; // 默认返回春季
        }
    }

    private boolean isEnvironmentChanged(Environment env) {
        // 检测温度、降水量、土壤肥力是否变化
        return Math.abs(env.getTemperature() - previousTemperature) > 2 ||
                Math.abs(env.getPrecipitation() - previousPrecipitation) > 10 ||
                Math.abs(env.getSoilFertility() - previousSoilFertility) > 0.1;
    }
}