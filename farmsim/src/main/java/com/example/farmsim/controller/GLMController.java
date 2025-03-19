package com.example.farmsim.controller;

import com.example.farmsim.model.dto.AgentRequest;
import com.example.farmsim.service.AgentService;
import com.example.farmsim.service.GLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/glm")
public class GLMController {


    @Autowired
    private AgentService agentService;

    @Autowired
    private GLMService glmService;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody AgentRequest request) {
        try {
            String simulationId = "当前模拟ID"; // 需根据实际逻辑获取
            String response;

            if (request.getPrompt() != null) {
                // 处理用户问题
                response = agentService.handleUserPrompt(simulationId, request.getPrompt());
            } else if (request.getGoal() != null) {
                // 处理生产目标
                response = agentService.handleUserGoal(simulationId, request.getGoal());
            } else {
                // 无输入时触发自动决策
                response = agentService.generateAutomaticDecision(simulationId);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("错误：" + e.getMessage());
        }
    }
}