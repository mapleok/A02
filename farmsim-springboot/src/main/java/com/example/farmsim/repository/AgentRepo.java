package com.example.farmsim.repository;

import com.example.farmsim.model.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepo extends JpaRepository<Agent, String> {

    @Query("SELECT a FROM Agent a WHERE a.simulation.id = :simulationId")
    List<Agent> findBySimulationId(@Param("simulationId") String simulationId);

    @Query("SELECT a FROM Agent a WHERE a.simulation.id = :simulationId AND a.roleType = :roleType")
    List<Agent> findBySimulationIdAndRoleType(@Param("simulationId") String simulationId, @Param("roleType") String roleType);
}
