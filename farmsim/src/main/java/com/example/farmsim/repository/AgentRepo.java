package com.example.farmsim.repository;

import com.example.farmsim.model.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepo extends JpaRepository<Agent, String> {
    List<Agent> findBySimulationId(String simulationId);
}