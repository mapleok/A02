package com.example.farmsim.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AgentDTO {
    private String agentId;
    private String agentName;
    private String roleType;
    private String currentAction;
    private Map<String, String> properties;
    private String simulationId;

    // Getters and Setters
}