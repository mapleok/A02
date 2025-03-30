package com.example.farmsim.service;

import com.example.farmsim.model.entity.AgentDialogue;
import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.repository.SimulationRepo;
import com.example.farmsim.repository.AgentDialogueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgentDialogueService {
    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private AgentDialogueRepo agentDialogueRepo;

    @Transactional // 添加事务注解确保操作原子性
    public void saveDialogue(String simulationId, String agentId, String roleType, String content) {
        Simulation simulation = simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));

        AgentDialogue dialogue = new AgentDialogue();
        dialogue.setAgentId(agentId);
        dialogue.setRole(roleType);
        dialogue.setContent(content);
        dialogue.setTimestamp(LocalDateTime.now());
        dialogue.setSimulation(simulation); // 确保关联关系正确设置

        agentDialogueRepo.save(dialogue); // 保存到数据库
    }

    public List<AgentDialogue> getDialogueHistory(String simulationId) {
        return agentDialogueRepo.findBySimulationId(simulationId);
    }
}