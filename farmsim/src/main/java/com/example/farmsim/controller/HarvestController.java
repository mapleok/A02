package com.example.farmsim.controller;

import com.example.farmsim.model.dto.HarvestDTO;
import com.example.farmsim.model.dto.HarvestResponseDTO;
import com.example.farmsim.model.entity.Harvest;
import com.example.farmsim.service.HarvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvest")
public class HarvestController {

    @Autowired
    private HarvestService harvestService;

    // 保存收成数据
    @PostMapping
    public ResponseEntity<?> createHarvest(@RequestBody HarvestDTO harvestDTO) {
        try {
            harvestService.saveHarvest(harvestDTO);
            return ResponseEntity.ok().body("收成数据保存成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("保存收成数据失败: " + e.getMessage());
        }
    }

    // 查询指定模拟的收成数据
    @GetMapping("/{simulationId}")
    public ResponseEntity<List<HarvestResponseDTO>> getHarvestsBySimulationId(@PathVariable String simulationId) {
        List<HarvestResponseDTO> harvests = harvestService.getHarvestsBySimulationId(simulationId);
        return ResponseEntity.ok(harvests);
    }
}