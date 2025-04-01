package com.example.farmsim.service;

import com.example.farmsim.model.dto.CropDTO;
import com.example.farmsim.model.dto.HarvestDTO;
import com.example.farmsim.model.entity.Crop;
import com.example.farmsim.model.entity.Harvest;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.model.entity.SimulationStatus;
import com.example.farmsim.repository.CropRepo;
import com.example.farmsim.websocket.Message;
import com.example.farmsim.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.farmsim.repository.SimulationRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

@Service
@Transactional
public class CropService {
    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private  HarvestService harvestService;

    @Autowired
    private AgentService agentService;
    // 默认坐标范围（可配置或从数据库读取）
    private static final float MIN_X = -10f;
    private static final float MAX_X = 10f;
    private static final float MIN_Z = -10f;
    private static final float MAX_Z = 10f;
    private static final float Y_POSITION = 0f; // 固定Y坐标

    public String createCrop(CropDTO dto) {
        // 1. 查找模拟
        Simulation simulation = simulationRepo.findById(dto.getSimulationId())
                .orElseThrow(() -> new RuntimeException("模拟ID " + dto.getSimulationId() + " 不存在"));

        // 2. 创建作物
        Crop crop = new Crop();
        crop.setCropId(UUID.randomUUID().toString());
        crop.setCropName(dto.getCropName());
        switch (dto.getCropType()) {
            case "玉米":
                crop.setGrowthRate(1.2);
                crop.setTempWeight(0.6);
                crop.setSoilWeight(0.4);
                crop.setWaterWeight(0.5);
                break;
            case "小麦":
                crop.setGrowthRate(1.0);
                crop.setTempWeight(0.5);
                crop.setSoilWeight(0.5);
                crop.setWaterWeight(0.4);
                break;
            case "南瓜":
                crop.setGrowthRate(0.8);
                crop.setTempWeight(0.4);
                crop.setSoilWeight(0.6);
                crop.setWaterWeight(0.6);
                break;
            default:
                // 默认值
                crop.setGrowthRate(dto.getGrowthRate());
                crop.setTempWeight(dto.getTempWeight());
                crop.setSoilWeight(dto.getSoilWeight());
                crop.setWaterWeight(dto.getWaterWeight());
        }
        crop.setPlantingDate(LocalDate.now());
        crop.setCropType(dto.getCropType());

        // 3. 计算成熟时间
        double environmentFactor = 1 + (dto.getTempWeight() + dto.getSoilWeight() + dto.getWaterWeight()) / 3;
        int maturityTime = (int) (100 / (dto.getGrowthRate() * environmentFactor));
        crop.setMaturityTime(maturityTime);

        // 4. 关联模拟并保存
        crop.setSimulation(simulation);
        cropRepo.save(crop);

        // 5. 生成随机坐标（或使用DTO中传入的坐标）
        float x = dto.getX() != null ? dto.getX() : generateRandomCoordinate(MIN_X, MAX_X);
        float z = dto.getZ() != null ? dto.getZ() : generateRandomCoordinate(MIN_Z, MAX_Z);

        // 6. 发送 WebSocket 消息
        Message message = new Message();
        message.setType("crop-created");
        message.setContent("作物创建成功");
        message.setSimulationId(dto.getSimulationId());
        message.setData(Map.of(
                "cropId", crop.getCropId(),
                "cropName", crop.getCropName(),
                "growthRate", crop.getGrowthRate(),
                "tempWeight", crop.getTempWeight(),
                "soilWeight", crop.getSoilWeight(),
                "waterWeight", crop.getWaterWeight(),
                "cropType", crop.getCropType(),
                "position", Map.of("x", x, "y", Y_POSITION, "z", z) // 使用生成的坐标
        ));
        webSocketHandler.broadcastMessage(message);

        return crop.getCropId();
    }

    // 生成随机坐标
    private float generateRandomCoordinate(float min, float max) {
        return min + new Random().nextFloat() * (max - min);
    }

    // CropService.java
    @Scheduled(fixedRate = 15000)
    public void checkCropMaturity() {
        List<Simulation> runningSimulations = simulationRepo.findByStatus(SimulationStatus.RUNNING);
        for (Simulation sim : runningSimulations) {
            List<Crop> crops = cropRepo.findBySimulationId(sim.getId());
            for (Crop crop : crops) {
                // 使用模拟天数计算成熟时间（而非实际时间）
                int daysPassed = sim.getSimulationDays();
                if (daysPassed >= crop.getMaturityTime() && !crop.isMature()) {
                    crop.setMature(true);
                    cropRepo.save(crop);
                    harvestCrop(crop);
                }
            }
        }
    }

    private void harvestCrop(Crop crop) {
        double yield = calculateCropYield(crop);
        double quality = calculateCropQuality(crop);
        Harvest harvest = new Harvest();
        harvest.setCropName(crop.getCropName());
        harvest.setYield(yield);
        harvest.setQuality(quality);
        harvest.setHarvestTime(LocalDateTime.now());
        harvest.setSimulation(crop.getSimulation());

        // 将 Harvest 转换为 HarvestDTO
        HarvestDTO harvestDTO = new HarvestDTO();
        harvestDTO.setCropName(harvest.getCropName());
        harvestDTO.setYield(harvest.getYield());
        harvestDTO.setQuality(harvest.getQuality());
        harvestDTO.setHarvestTime(harvest.getHarvestTime());
        harvestDTO.setSimulationId(harvest.getSimulation().getId());

        harvestService.saveHarvest(harvestDTO);
    }

    private double calculateCropYield(Crop crop) {
        // 计算产量的逻辑
        return crop.getGrowthRate() * 100; // 示例计算
    }

    private double calculateCropQuality(Crop crop) {
        // 计算质量的逻辑
        return crop.getGrowthRate() * 0.8; // 示例计算
    }
}