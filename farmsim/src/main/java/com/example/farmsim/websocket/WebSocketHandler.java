package com.example.farmsim.websocket;

import com.example.farmsim.service.GLMService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final GLMService glmService;
    private final ObjectMapper mapper = new ObjectMapper();

    public WebSocketHandler(GLMService glmService) {
        this.glmService = glmService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            // 1. 解析客户端消息
            Message msg = mapper.readValue(message.getPayload(), Message.class);
            String type = msg.getType();
            String simulationId = msg.getSimulationId();
            String prompt = (String) msg.getData().get("prompt");

            // 2. 构建对话上下文
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content",
                    "你是一个农业专家，请根据以下问题提供建议。"));
            messages.add(Map.of("role", "user", "content", prompt));

            if ("start-dialogue".equals(type)) {
                // 3. 调用大模型生成响应
                String response = glmService.getResponseFromGLM(messages);

                // 4. 发送自然语言响应
                Message responseMessage = new Message();
                responseMessage.setType("dialogue-response");
                responseMessage.setContent(response);
                session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
            }
        } catch (Exception e) {
            // 错误处理
            session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"服务器错误: " + e.getMessage() + "\"}"));
        }
    }
}