package com.example.farmsim.repository;

import com.example.farmsim.model.entity.RevenueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RevenueRecordRepo extends JpaRepository<RevenueRecord, Long> {
    List<RevenueRecord> findBySimulationId(String simulationId);
}