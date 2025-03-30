package com.example.farmsim.service;

import com.example.farmsim.model.entity.*;
import com.example.farmsim.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GLMService {

    private static final Logger logger = LoggerFactory.getLogger(GLMService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("https://open.bigmodel.cn/api/paas/v4/chat/completions") // 从配置文件中读取 API URL
    private String glmApiUrl;

    @Value("92d415b6fd5242ec8d90d3ff7472df5f.JJgtdsqci7TGYWhq") // 从配置文件中读取 API 密钥
    private String apiKey;

    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private UserGoalRepo userGoalRepo;

    @Autowired
    private AgentCommandRepo agentCommandRepo;

    // GLMService.java
    public String getResponseFromGLM(List<Map<String, String>> messages) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "glm-4");
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(glmApiUrl, HttpMethod.POST, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            return rootNode.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            logger.error("Error while calling GLM API: {}", e.getMessage());
            throw new RuntimeException("Failed to call GLM API", e);
        }
    }
}