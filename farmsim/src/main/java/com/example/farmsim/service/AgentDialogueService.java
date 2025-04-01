package com.example.farmsim.service;

import com.example.farmsim.model.dto.AgentDialogueDTO;
import com.example.farmsim.model.entity.AgentDialogue;
import com.example.farmsim.repository.SimulationRepo;
import com.example.farmsim.repository.AgentDialogueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentDialogueService {
    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private AgentDialogueRepo agentDialogueRepo;

    // AgentDialogueService.java
    public List<AgentDialogueDTO> getDialogueHistory(String simulationId) {
        return agentDialogueRepo.findBySimulationId(simulationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 使用Builder模式的转换方法
    private AgentDialogueDTO convertToDTO(AgentDialogue entity) {
        return AgentDialogueDTO.builder()
                .id(entity.getId())
                .agentId(entity.getAgentId())
                .roleType(entity.getRoleType()) // 确保实体类有此方法
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .build();
    }

    // AgentDialogueService.java 添加分页查询
    public List<AgentDialogue> getLatestDialogueHistory(String simulationId, int limit) {
        return agentDialogueRepo.findBySimulationIdOrderByTimestampDesc(
                simulationId,
                PageRequest.of(0, limit)
        );
    }
}