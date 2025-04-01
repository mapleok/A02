package com.example.farmsim.service;

import com.example.farmsim.model.dto.EnvironmentDTO;
import com.example.farmsim.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/environments")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    // 创建环境
    @PostMapping
    public ResponseEntity<Long> createEnvironment(@RequestBody EnvironmentDTO dto) {
        try {
            Long environmentId = environmentService.createEnvironment(dto);
            return ResponseEntity.ok(environmentId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(-1L);
        }
    }

    // 创建自定义环境
    @PostMapping("/custom")
    public ResponseEntity<Long> createCustomEnvironment(@RequestBody EnvironmentDTO dto) {
        try {
            Long environmentId = environmentService.createCustomEnvironment(dto);
            return ResponseEntity.ok(environmentId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(-1L);
        }
    }

    // 获取最新环境数据
    @GetMapping("/latest")
    public ResponseEntity<EnvironmentDTO> getLatestEnvironment(@RequestParam String simulationId) {
        try {
            EnvironmentDTO latestEnv = environmentService.getLatestEnvironment(simulationId);
            return ResponseEntity.ok(latestEnv);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}