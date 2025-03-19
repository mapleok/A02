package com.example.farmsim.repository;

import com.example.farmsim.model.entity.AgentCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentCommandRepo extends JpaRepository<AgentCommand, Long> {
    List<AgentCommand> findBySimulationId(String simulationId);
}