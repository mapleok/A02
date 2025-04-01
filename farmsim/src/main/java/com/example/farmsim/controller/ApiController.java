package com.example.farmsim.controller;

import com.example.farmsim.model.dto.*;
import com.example.farmsim.model.entity.*;
import com.example.farmsim.repository.CropRepo;
import com.example.farmsim.repository.EnvironmentRepo;
import com.example.farmsim.repository.RevenueRecordRepo;
import com.example.farmsim.repository.SimulationRepo;
import com.example.farmsim.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.example.farmsim.service.AgentDialogueService;

import javax.servlet.http.HttpSession;
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

    @Autowired
    private SimulationRepo simulationRepo;

    @PostMapping("/simulation/{id}/time-scale")
    public ResponseEntity<?> setTimeScale(
            @PathVariable String id,
            @RequestBody Map<String, Integer> request
    ) {
        Simulation sim = simulationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("模拟不存在"));
        sim.setTimeScale(request.get("scale"));
        simulationRepo.save(sim);
        return ResponseEntity.ok(Map.of("message", "时间加速设置成功"));
    }

    @PostMapping("/simulation/{id}/toggle-pause")
    public ResponseEntity<?> togglePause(@PathVariable String id) {
        Simulation sim = simulationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("模拟不存在"));
        sim.setStatus(
                sim.getStatus() == SimulationStatus.RUNNING ?
                        SimulationStatus.PAUSED : SimulationStatus.RUNNING
        );
        simulationRepo.save(sim);
        return ResponseEntity.ok(Map.of(
                "newStatus", sim.getStatus().name()
        ));
    }

    @PostMapping("/simulation")
    public ResponseEntity<Map<String, String>> createSimulation(
            @RequestBody SimulationDTO dto,
            HttpSession session // 确保注入 Session
    ) {
        String simulationId = simulationService.createSimulation(dto);
        session.setAttribute("currentSimulationId", simulationId);
        // 设置 Cookie 有效期（可选）
        session.setMaxInactiveInterval(36000);
        return ResponseEntity.ok(Map.of("id", simulationId));
    }

    // 获取当前模拟的JSON指令
    @PostMapping("/simulation/get-json")
    public ResponseEntity<?> getJson(HttpSession session) {
        try {
            String simulationId = validateSession(session);
            String jsonCommand = agentService.generateJsonCommand(simulationId);
            JsonNode jsonNode = new ObjectMapper().readTree(jsonCommand);
            return ResponseEntity.ok(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("错误：" + e.getMessage());
        }
    }

    // 获取模拟信息
    @GetMapping("/simulation")
    public ResponseEntity<Map<String, String>> getSimulation(HttpSession session) {
        try {
            String simulationId = validateSession(session);
            SimulationDTO simulation = simulationService.getSimulation(simulationId);
            Map<String, String> response = new HashMap<>();
            response.put("id", simulation.getId());
            response.put("name", simulation.getName());
            response.put("description", simulation.getDescription());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 结束当前模拟
    @PostMapping("/simulation/end")
    public ResponseEntity<String> endSimulation(HttpSession session) {
        try {
            String simulationId = validateSession(session);
            simulationService.endSimulation(simulationId);
            session.removeAttribute("currentSimulationId"); // 清除会话中的ID
            return ResponseEntity.ok("模拟已结束，收成数据已计算并保存。");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("结束模拟失败: " + e.getMessage());
        }
    }

    // 创建Agent（自动关联当前模拟）
    @PostMapping("/agent")
    public ResponseEntity<?> createAgent(@RequestBody AgentDTO dto, HttpSession session) {
        try {
            String simulationId = validateSession(session);
            agentService.createAgent(dto.getAgentName(), simulationId, dto.getRoleType());
            return ResponseEntity.ok(Map.of("message", "Agent创建成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 启动对话（自动关联当前模拟）
    @PostMapping("/start-dialogue")
    public ResponseEntity<?> startDialogue(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            String simulationId = validateSession(session);
            String response = agentService.startDialogue(simulationId, request.get("prompt"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("错误：" + e.getMessage());
        }
    }

    // 随机天气（自动关联当前模拟）
    @PostMapping("/random-weather")
    public ResponseEntity<String> randomizeWeather(HttpSession session) {
        try {
            String simulationId = validateSession(session);
            Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);

            latestEnv.setTemperature(Math.max(-50, Math.min(50, latestEnv.getTemperature() + (Math.random() * 4 - 2))));
            latestEnv.setPrecipitation(Math.max(0, latestEnv.getPrecipitation() + (Math.random() * 20 - 10)));
            latestEnv.setSoilFertility(Math.max(0, Math.min(1, latestEnv.getSoilFertility() + (Math.random() * 0.1 - 0.05))));

            environmentRepo.save(latestEnv);
            return ResponseEntity.ok("天气已随机更新");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("操作失败: " + e.getMessage());
        }
    }

    // 创建农作物（自动关联当前模拟）
    @PostMapping("/crop")
    public ResponseEntity<?> createCrop(@RequestBody CropDTO dto, HttpSession session) {
        try {
            String simulationId = validateSession(session);
            dto.setSimulationId(simulationId);
            String cropId = cropService.createCrop(dto);
            return ResponseEntity.ok(Map.of("id", cropId, "message", "作物创建成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 获取收益对比（自动关联当前模拟）
    @GetMapping("/revenue-comparison")
    public ResponseEntity<?> getRevenueComparison(HttpSession session) {
        try {
            String simulationId = validateSession(session);
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 启用专家模式（自动关联当前模拟）
    @PostMapping("/enable-expert-mode")
    public ResponseEntity<String> enableExpertMode(HttpSession session) {
        try {
            String simulationId = validateSession(session);

            // 1. 创建农业专家Agent
            agentService.createAgronomist(simulationId);

            // 2. 修正方法名：调用 autoTriggerDialogue 而非 triggerAgentDialogue
            agentService.autoTriggerDialogue(simulationId);

            return ResponseEntity.ok("农业专家模式已开启，对话已触发");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("操作失败: " + e.getMessage());
        }
    }

    // 设置时间速率（自动关联当前模拟）
    @PostMapping("/set-time-scale")
    public ResponseEntity<String> setTimeScale(@RequestParam int timeScale, HttpSession session) {
        try {
            String simulationId = validateSession(session);
            Simulation sim = simulationRepo.findById(simulationId)
                    .orElseThrow(() -> new RuntimeException("模拟不存在"));
            sim.setTimeScale(timeScale);
            simulationRepo.save(sim);
            return ResponseEntity.ok("时间速率已设置为 1 分钟 = " + timeScale + " 天");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("操作失败: " + e.getMessage());
        }
    }

    // 获取作物列表（自动关联当前模拟）
    @GetMapping("/crops")
    public ResponseEntity<?> getCrops(HttpSession session) {
        try {
            String simulationId = validateSession(session);
            List<Crop> crops = cropRepo.findBySimulationId(simulationId);
            return ResponseEntity.ok(crops);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取失败: " + e.getMessage());
        }
    }

    // 获取对话历史（自动关联当前模拟）
    @GetMapping("/dialogue-history")
    public ResponseEntity<?> getDialogueHistory(HttpSession session) {
        try {
            String simulationId = validateSession(session);
            List<AgentDialogueDTO> history = agentDialogueService.getDialogueHistory(simulationId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取失败: " + e.getMessage());
        }
    }

    // ApiController.java
    @PostMapping("/trigger-auto-dialogue")
    public ResponseEntity<String> triggerAutoDialogue(@RequestParam String simulationId) {
        try {
            agentService.autoTriggerDialogue(simulationId);
            return ResponseEntity.ok("自动对话已触发");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("触发自动对话失败: " + e.getMessage());
        }
    }

    // 会话校验方法
    private String validateSession(HttpSession session) {
        String simulationId = (String) session.getAttribute("currentSimulationId");
        if (simulationId == null) {
            throw new IllegalArgumentException("当前没有活跃的模拟");
        }
        return simulationId;
    }
}