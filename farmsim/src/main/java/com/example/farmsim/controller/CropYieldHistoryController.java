// src/main/java/com/example/farmsim/controller/CropYieldHistoryController.java
package com.example.farmsim.controller;

import com.example.farmsim.model.dto.CropYieldHistoryDTO;
import com.example.farmsim.model.entity.CropYieldHistory;
import com.example.farmsim.repository.CropYieldHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// src/main/java/com/example/farmsim/controller/CropYieldHistoryController.java
@RestController
@RequestMapping("/api/crop-yield-history")
public class CropYieldHistoryController {
    @Autowired
    private CropYieldHistoryRepo cropYieldHistoryRepo;

    @GetMapping("/{cropId}/{simulationId}")
    public ResponseEntity<List<CropYieldHistoryDTO>> getCropYieldHistory(
            @PathVariable String cropId,
            @PathVariable String simulationId) {
        List<CropYieldHistory> history = cropYieldHistoryRepo.findByCropIdAndSimulationId(cropId, simulationId);

        List<CropYieldHistoryDTO> result = history.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    private CropYieldHistoryDTO convertToDTO(CropYieldHistory history) {
        CropYieldHistoryDTO dto = new CropYieldHistoryDTO();
        dto.setId(history.getId());
        dto.setCropId(history.getCropId());
        dto.setYield(history.getYield());
        dto.setTimestamp(history.getTimestamp());
        return dto;
    }
}