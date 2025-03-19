package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

// src/main/java/com/example/farmsim/model/dto/AgentRequest.java
@Getter
@Setter
public class AgentRequest {
    private String prompt;          // 用户问题（可选）
    private Map<String, Object> goal; // 生产目标（可选），如 {"targetYield": 500, "cropType": "小麦"}
}