package com.example.farmsim.repository;

import com.example.farmsim.model.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentRepo extends JpaRepository<Environment, Long> {
    // 查找指定模拟的最新环境数据
    @Query("SELECT e FROM Environment e WHERE e.simulation.id = :simulationId ORDER BY e.timestamp DESC")
    Environment findTopBySimulationIdOrderByTimestampDesc(@Param("simulationId") String simulationId);
}