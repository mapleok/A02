// src/main/java/com/example/farmsim/repository/CropYieldHistoryRepo.java
package com.example.farmsim.repository;

import com.example.farmsim.model.entity.CropYieldHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CropYieldHistoryRepo extends JpaRepository<CropYieldHistory, Long> {
    List<CropYieldHistory> findByCropIdAndSimulationId(String cropId, String simulationId); // 按农作物 ID 查询历史数据
}