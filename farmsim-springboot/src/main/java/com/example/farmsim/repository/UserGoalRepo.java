package com.example.farmsim.repository;

import com.example.farmsim.model.entity.UserGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// src/main/java/com/example/farmsim/repository/UserGoalRepo.java
public interface UserGoalRepo extends JpaRepository<UserGoal, Long> {
    List<UserGoal> findBySimulationId(String simulationId);
}