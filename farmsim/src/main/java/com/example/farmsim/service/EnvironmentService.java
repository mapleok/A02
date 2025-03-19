package com.example.farmsim.service;

import com.example.farmsim.model.dto.EnvironmentDTO;
import com.example.farmsim.model.entity.Environment;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.repository.EnvironmentRepo;
import com.example.farmsim.repository.SimulationRepo;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

// EnvironmentService.java
@Service
public class EnvironmentService {
    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    public Long createEnvironment(EnvironmentDTO dto) {
        // 1. 查找关联的模拟
        Simulation simulation = simulationRepo.findById(dto.getSimulationId())
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + dto.getSimulationId()));

        // 2. 创建环境对象
        Environment env = new Environment();
        env.setTemperature(dto.getTemperature());
        env.setSoilFertility(dto.getSoilFertility() / 100.0); // 将百分比转换为小数
        env.setPrecipitation(dto.getPrecipitation());
        env.setTerrain(dto.getTerrain());
        env.setClimate(dto.getClimate());
        env.setAgriculturalTechnology(dto.getAgriculturalTechnology());
        env.setTimestamp(LocalDateTime.now());
        env.setSimulation(simulation);

        // 3. 保存数据
        environmentRepo.save(env);
        return env.getId();
    }
}

