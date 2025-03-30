package com.example.farmsim.repository;

import com.example.farmsim.model.entity.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestRepo extends JpaRepository<Harvest, Long> {
    // 在 HarvestRepo 中定义查询方法
    @Query("SELECT h FROM Harvest h WHERE h.simulation.id = :simulationId")
    List<Harvest> findBySimulationId(@Param("simulationId") String simulationId);
}