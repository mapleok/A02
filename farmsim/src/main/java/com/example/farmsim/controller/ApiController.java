package com.example.farmsim.controller;

import com.example.farmsim.model.dto.*;
import com.example.farmsim.model.entity.AgentDialogue;
import com.example.farmsim.model.entity.Crop;
import com.example.farmsim.model.entity.Environment;
import com.example.farmsim.model.entity.RevenueRecord;
import com.example.farmsim.repository.CropRepo;
import com.example.farmsim.repository.EnvironmentRepo;
import com.example.farmsim.repository.RevenueRecordRepo;

import com.example.farmsim.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.example.farmsim.service.AgentDialogueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private SimulationService simulationService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private CropService cropService;

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private AgentDialogueService agentDialogueService;

    @Autowired
    private RevenueRecordRepo revenueRecordRepo;

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private CropRepo cropRepo;

    // 创建模拟
    @PostMapping("/simulation")
    public ResponseEntity<Map<String, String>> createSimulation(@RequestBody SimulationDTO dto) {
        String simulationId = simulationService.createSimulation(dto);
        Map<String, String> response = new HashMap<>();
        response.put("id", simulationId);
        response.put("message", "Simulation created successfully");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/simulation/{simulationId}/get-json")
    public ResponseEntity<?> getJson(@PathVariable String simulationId) {
        try {
            // 1. 调用 AgentService 生成 JSON 指令
            String jsonCommand = agentService.generateJsonCommand(simulationId);

            // 2. 解析 JSON 字符串为 JsonNode（确保格式正确）
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonCommand);

            // 3. 返回 JSON 响应
            return ResponseEntity.ok(jsonNode);
        } catch (Exception e) {
            // 4. 错误处理
            return ResponseEntity.status(500).body("错误：" + e.getMessage());
        }
    }
    // 获取模拟
    @GetMapping("/simulation/{id}")
    public ResponseEntity<Map<String, String>> getSimulation(@PathVariable String id) {
        SimulationDTO simulation = simulationService.getSimulation(id);
        Map<String, String> response = new HashMap<>();
        response.put("id", simulation.getId());
        response.put("name", simulation.getName());
        response.put("description", simulation.getDescription());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simulation/{simulationId}/end")
    public ResponseEntity<String> endSimulation(@PathVariable String simulationId) {
        try {
            simulationService.endSimulation(simulationId);
            return ResponseEntity.ok("模拟已结束，收成数据已计算并保存。");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("结束模拟失败: " + e.getMessage());
        }
    }

    // 创建 Agent
    @PostMapping("/agent")
    public ResponseEntity<Map<String, String>> createAgent(@RequestBody AgentDTO dto) {
        try {
            // 1. 验证输入参数
            if (StringUtils.isEmpty(dto.getAgentName())) {
                throw new IllegalArgumentException("Agent 名称不能为空");
            }
            if (StringUtils.isEmpty(dto.getSimulationId())) {
                throw new IllegalArgumentException("模拟 ID 不能为空");
            }

            // 2. 调用服务层创建 Agent
            agentService.createAgent(dto.getAgentName(), dto.getSimulationId());

            // 3. 返回成功响应
            Map<String, String> response = new HashMap<>();
            response.put("message", "Agent 创建成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 4. 处理参数错误
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            // 5. 处理其他异常
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "创建 Agent 失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/simulation/{simulationId}/start-dialogue")
    public String startDialogue(
            @PathVariable String simulationId,
            @RequestBody Map<String, String> request) {
        // 调用修改后的无JSON版本
        return agentService.startDialogue(simulationId, request.get("prompt"));
    }

    // EnvironmentController.java
    @PostMapping("/random-weather/{simulationId}")
    public ResponseEntity<String> randomizeWeather(@PathVariable String simulationId) {
        Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);

        // 实现随机变化逻辑（示例）
        latestEnv.setTemperature(latestEnv.getTemperature() + (Math.random() * 4 - 2));
        latestEnv.setPrecipitation(latestEnv.getPrecipitation() + (Math.random() * 20 - 10));
        latestEnv.setSoilFertility(latestEnv.getSoilFertility() + (Math.random() * 0.1 - 0.05));

        // 约束取值范围
        latestEnv.setTemperature(Math.max(-50, Math.min(50, latestEnv.getTemperature())));
        latestEnv.setPrecipitation(Math.max(0, latestEnv.getPrecipitation()));
        latestEnv.setSoilFertility(Math.max(0, Math.min(1, latestEnv.getSoilFertility())));

        environmentRepo.save(latestEnv);
        return ResponseEntity.ok("天气已随机更新");
    }

    @PostMapping("/agent/decision")
    public ResponseEntity<?> makeAgentDecision(
            @RequestParam String agentId,
            @RequestParam String prompt
    ) {
        try {
            String response = agentService.makeDecision(agentId, prompt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("错误：" + e.getMessage());
        }
    }

    // 创建农作物
    @PostMapping("/crop")
    public ResponseEntity<Map<String, String>> createCrop(@RequestBody CropDTO dto) {
        String cropId = cropService.createCrop(dto);
        Map<String, String> response = new HashMap<>();
        response.put("id", cropId);
        response.put("message", "Crop created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/simulation/{simulationId}/revenue-comparison")
    public ResponseEntity<Map<String, Double>> getRevenueComparison(@PathVariable String simulationId) {
        List<RevenueRecord> records = revenueRecordRepo.findBySimulationId(simulationId);

        double beforeExpert = records.stream()
                .filter(r -> !r.isExpertModeEnabled())
                .mapToDouble(RevenueRecord::getRevenue)
                .average().orElse(0);

        double afterExpert = records.stream()
                .filter(r -> r.isExpertModeEnabled())
                .mapToDouble(RevenueRecord::getRevenue)
                .average().orElse(0);

        Map<String, Double> result = new HashMap<>();
        result.put("beforeExpert", beforeExpert);
        result.put("afterExpert", afterExpert);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/simulation/{simulationId}/enable-expert-mode")
    public ResponseEntity<String> enableExpertMode(@PathVariable String simulationId) {
        agentService.createAgronomist(simulationId);
        agentService.triggerAgentDialogue(simulationId); // 新增：触发对话
        return ResponseEntity.ok("农业专家模式已开启，对话已触发");
    }


    @PostMapping("/environment")
    public ResponseEntity<Map<String, String>> createEnvironment(@RequestBody EnvironmentDTO dto) {
        Long environmentId = environmentService.createEnvironment(dto);
        Map<String, String> response = new HashMap<>();
        response.put("id", environmentId.toString());
        response.put("message", "Environment created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/crops")
    public ResponseEntity<List<Crop>> getCropsBySimulationId(@RequestParam String simulationId) {
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);
        return ResponseEntity.ok(crops);
    }


    @GetMapping("/simulation/{id}/dialogue-history")
    public ResponseEntity<List<AgentDialogue>> getDialogueHistory(@PathVariable String id) {
        List<AgentDialogue> history = agentDialogueService.getDialogueHistory(id);
        return ResponseEntity.ok(history);
    }
}