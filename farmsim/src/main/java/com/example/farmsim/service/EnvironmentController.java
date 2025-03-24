package com.example.farmsim.service;

import com.example.farmsim.model.dto.EnvironmentDTO;
import com.example.farmsim.model.entity.Environment;
import com.example.farmsim.repository.EnvironmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/environments")
public class EnvironmentController {

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private EnvironmentService environmentService;

    @GetMapping("/latest")
    public ResponseEntity<EnvironmentDTO> getLatestEnvironment(@RequestParam String simulationId) {
        // 获取指定模拟的最新环境数据
        Environment latest = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        if (latest == null) {
            return ResponseEntity.notFound().build(); // 如果未找到数据，返回 404
        }

        // 将环境数据转换为 DTO
        EnvironmentDTO dto = new EnvironmentDTO();
        dto.setTemperature(latest.getTemperature());
        dto.setSoilFertility(latest.getSoilFertility());
        dto.setPrecipitation(latest.getPrecipitation());
        dto.setTimestamp(latest.getTimestamp());

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/custom")
    public ResponseEntity<Long> createCustomEnvironment(@RequestBody EnvironmentDTO dto) {
        try {
            Long environmentId = environmentService.createCustomEnvironment(dto);
            return ResponseEntity.ok(environmentId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(-1L);
        }
    }

    @PostMapping
    public ResponseEntity<Long> createEnvironment(@RequestBody EnvironmentDTO dto) {
        Long environmentId = environmentService.createEnvironment(dto);
        return ResponseEntity.ok(environmentId);
    }
}