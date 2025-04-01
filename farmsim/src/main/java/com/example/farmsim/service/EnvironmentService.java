package com.example.farmsim.service;

import com.example.farmsim.model.dto.EnvironmentDTO;
import com.example.farmsim.model.entity.Environment;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.model.entity.SimulationStatus;
import com.example.farmsim.model.entity.WeatherModel;
import com.example.farmsim.repository.EnvironmentRepo;
import com.example.farmsim.repository.SimulationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvironmentService {

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    // 获取当前模拟ID的逻辑
    public String getCurrentSimulationId() {
        // 假设模拟 ID 存储在请求头中
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String simulationId = request.getHeader("X-Simulation-Id");
            if (simulationId == null || simulationId.isEmpty()) {
                throw new IllegalArgumentException("当前模拟 ID 未设置");
            }
            return simulationId;
        }
        throw new RuntimeException("无法获取当前请求的模拟 ID");
    }

    // 创建环境的方法
    public Long createEnvironment(EnvironmentDTO dto) {
        try {
            // 1. 获取当前模拟ID
            String simulationId = getCurrentSimulationId();

            // 2. 查找关联的模拟
            Simulation simulation = simulationRepo.findById(simulationId)
                    .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));

            // 3. 创建环境对象
            Environment env = new Environment();
            env.setTemperature(dto.getTemperature());
            env.setSoilFertility(dto.getSoilFertility() / 100.0); // 将百分比转换为小数
            env.setPrecipitation(dto.getPrecipitation());
            env.setTerrain(dto.getTerrain());
            env.setClimate(dto.getClimate());
            env.setAgriculturalTechnology(dto.getAgriculturalTechnology());
            env.setTimestamp(LocalDateTime.now());
            env.setSimulation(simulation); // 关联当前模拟

            // 4. 保存数据
            environmentRepo.save(env);
            return env.getId();
        } catch (Exception e) {
            throw new RuntimeException("创建环境失败: " + e.getMessage());
        }
    }

    // 创建自定义环境的方法
    public Long createCustomEnvironment(EnvironmentDTO dto) {
        try {
            // 1. 获取当前模拟ID
            String simulationId = getCurrentSimulationId();

            // 2. 查找关联的模拟
            Simulation simulation = simulationRepo.findById(simulationId)
                    .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));

            // 3. 数据校验
            if (dto.getTemperature() < -50 || dto.getTemperature() > 50) {
                throw new IllegalArgumentException("温度应在 -50℃ 到 50℃ 之间");
            }
            if (dto.getSoilFertility() < 0 || dto.getSoilFertility() > 100) {
                throw new IllegalArgumentException("土壤肥力应在 0% 到 100% 之间");
            }
            if (dto.getPrecipitation() < 0) {
                throw new IllegalArgumentException("降水量不能为负数");
            }
            if (dto.getTerrain() == null || dto.getTerrain().isEmpty()) {
                throw new IllegalArgumentException("地形不能为空");
            }
            if (dto.getClimate() == null || dto.getClimate().isEmpty()) {
                throw new IllegalArgumentException("气候不能为空");
            }
            if (dto.getAgriculturalTechnology() == null || dto.getAgriculturalTechnology().isEmpty()) {
                throw new IllegalArgumentException("农业技术不能为空");
            }

            // 4. 创建环境对象
            Environment env = new Environment();
            env.setTemperature(dto.getTemperature());
            env.setSoilFertility(dto.getSoilFertility() / 100.0); // 将百分比转换为小数
            env.setPrecipitation(dto.getPrecipitation());
            env.setTerrain(dto.getTerrain());
            env.setClimate(dto.getClimate());
            env.setAgriculturalTechnology(dto.getAgriculturalTechnology());
            env.setTimestamp(LocalDateTime.now());
            env.setSimulation(simulation); // 关联当前模拟

            // 5. 保存数据
            environmentRepo.save(env);
            return env.getId();
        } catch (Exception e) {
            throw new RuntimeException("创建自定义环境失败: " + e.getMessage());
        }
    }

    public EnvironmentDTO getLatestEnvironment(String simulationId) {
        Environment latest = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        if (latest == null) {
            throw new RuntimeException("未找到环境数据");
        }

        // 将环境数据转换为 DTO
        EnvironmentDTO dto = new EnvironmentDTO();
        dto.setTemperature(latest.getTemperature());
        dto.setSoilFertility(latest.getSoilFertility() * 100); // 转换为百分比
        dto.setPrecipitation(latest.getPrecipitation());
        dto.setTerrain(latest.getTerrain());
        dto.setClimate(latest.getClimate());
        dto.setAgriculturalTechnology(latest.getAgriculturalTechnology());
        dto.setTimestamp(latest.getTimestamp());

        return dto;
    }

    @Scheduled(fixedRate = 5000)
    public void updateEnvironment() {
        List<Simulation> runningSimulations = simulationRepo.findByStatus(SimulationStatus.RUNNING);
        for (Simulation sim : runningSimulations) {
            Environment newEnv = new Environment();
            newEnv.setSimulation(sim);

            // 传入当前模拟对象 sim
            String season = calculateSeason(sim);
            Environment weather = WeatherModel.generateWeather(season);

            newEnv.setTemperature(weather.getTemperature());
            newEnv.setPrecipitation(weather.getPrecipitation());
            newEnv.setTimestamp(LocalDateTime.now());
            environmentRepo.save(newEnv);
        }
    }

    private String calculateSeason(Simulation simulation) {
        // 使用模拟内部时间（模拟天数）计算季节
        long daysSinceStart = simulation.getSimulationDays();
        int seasonIndex = (int) (daysSinceStart / 90) % 4;
        switch (seasonIndex) {
            case 0: return "spring";
            case 1: return "summer";
            case 2: return "autumn";
            case 3: return "winter";
            default: return "spring";
        }
    }

}