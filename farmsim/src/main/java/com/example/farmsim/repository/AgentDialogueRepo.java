package com.example.farmsim.repository;

import com.example.farmsim.model.entity.AgentDialogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentDialogueRepo extends JpaRepository<AgentDialogue, Long> {

    // 根据 simulationId 查找对话记录
    @Query("SELECT d FROM AgentDialogue d WHERE d.simulation.id = :simulationId")
    List<AgentDialogue> findBySimulationId(@Param("simulationId") String simulationId);

    // 根据 agentId 查找最新的对话记录
    @Query("SELECT d FROM AgentDialogue d WHERE d.agentId = :agentId ORDER BY d.timestamp DESC")
    List<AgentDialogue> findTopByAgentIdOrderByTimestampDesc(@Param("agentId") String agentId);

    @Query("SELECT d FROM AgentDialogue d WHERE d.simulation.id = :simulationId ORDER BY d.timestamp DESC")
    List<AgentDialogue> findBySimulationIdOrderByTimestampDesc(
            @Param("simulationId") String simulationId,
            Pageable pageable
    );

    @Query("SELECT d FROM AgentDialogue d WHERE d.simulation.id = :simulationId ORDER BY d.timestamp DESC")
    List<AgentDialogue> findTop2BySimulationIdOrderByTimestampDesc(@Param("simulationId") String simulationId, Pageable pageable);

    // 查询当前模拟的最大对话轮次
    // AgentDialogueRepo.java
    @Query("SELECT MAX(d.dialogueRound) FROM AgentDialogue d WHERE d.simulation.id = :simulationId")
    Optional<Integer> findMaxDialogueRoundBySimulationId(@Param("simulationId") String simulationId);

    default Optional<AgentDialogue> findLatestByAgentId(String agentId) {
        List<AgentDialogue> dialogues = findTopByAgentIdOrderByTimestampDesc(agentId);
        return dialogues.isEmpty() ? Optional.empty() : Optional.of(dialogues.get(0));
    }
}