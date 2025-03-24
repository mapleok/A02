package com.example.farmsim.service;

import com.example.farmsim.model.dto.CropDTO;
import com.example.farmsim.model.entity.Crop;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.repository.CropRepo;
import com.example.farmsim.websocket.Message;
import com.example.farmsim.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.farmsim.repository.SimulationRepo;

import java.util.Map;
import java.util.UUID;

@Service
public class CropService {
    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private WebSocketHandler webSocketHandler;

    public String createCrop(CropDTO dto) {
        Crop crop = new Crop();
        crop.setCropId(UUID.randomUUID().toString());
        crop.setCropName(dto.getCropName());
        crop.setGrowthRate(dto.getGrowthRate());
        crop.setTempWeight(dto.getTempWeight());
        crop.setSoilWeight(dto.getSoilWeight());
        crop.setWaterWeight(dto.getWaterWeight());

        double environmentFactor = 1 + (dto.getTempWeight() + dto.getSoilWeight() + dto.getWaterWeight()) / 3;
        int maturityTime = (int) (100 / (dto.getGrowthRate() * environmentFactor)); // 基础成熟时间为 100 天
        crop.setMaturityTime(maturityTime);

        // 关联模拟
        Simulation simulation = simulationRepo.findById(dto.getSimulationId())
                .orElseThrow(() -> new RuntimeException("模拟ID " + dto.getSimulationId() + " 不存在"));
        crop.setSimulation(simulation);

        // 保存作物
        cropRepo.save(crop);

        // 构建 WebSocket 消息
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
                "waterWeight", crop.getWaterWeight()
        ));

        // 通过 WebSocket 发送消息
        webSocketHandler.broadcastMessage(message);

        return crop.getCropId();
    }
}