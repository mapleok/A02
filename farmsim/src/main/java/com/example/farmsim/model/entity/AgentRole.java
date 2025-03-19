// AgentRole.java
package com.example.farmsim.model.entity;

import lombok.Getter;

@Getter
public enum AgentRole {
    FARMER("农民", "负责作物日常管理"),
    METEOROLOGIST("气象专家", "分析天气对农业的影响"),
    AGRONOMIST("农业专家", "提供种植技术和肥料建议");

    private final String roleName;
    private final String description;

    AgentRole(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

}