package com.example.farmsim.websocket;

import com.example.farmsim.service.GLMService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
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

            switch (type) {
                case "start-dialogue":
                    handleStartDialogue(session, msg);
                    break;
                case "agent-decision":
                    handleAgentDecision(session, msg);
                    break;
                case "agent-automatic-decision":
                    handleAgentAutomaticDecision(session, msg);
                    break;
                case "auto-dialogue": // 新增：处理自动对话消息
                    handleAutoDialogue(session, msg);
                    break;
                default:
                    session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"未知消息类型: " + type + "\"}"));
                    break;
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"服务器错误: " + e.getMessage() + "\"}"));
        }
    }

    private void handleStartDialogue(WebSocketSession session, Message msg) throws IOException {
        String prompt = (String) msg.getData().get("prompt");

        // 构建对话上下文
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "你是一个农业专家，请根据以下问题提供建议。"));
        messages.add(Map.of("role", "user", "content", prompt));

        // 调用大模型生成响应
        String response = glmService.getResponseFromGLM(messages);

        // 发送自然语言响应
        Message responseMessage = new Message();
        responseMessage.setType("dialogue-response");
        responseMessage.setContent(response);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
    }

    private void handleAgentDecision(WebSocketSession session, Message msg) throws IOException {
        String agentId = (String) msg.getData().get("agentId");
        String decision = (String) msg.getData().get("decision");

        // 处理 Agent 决策
        System.out.println("Agent " + agentId + " 的决策: " + decision);

        // 发送确认消息
        Message responseMessage = new Message();
        responseMessage.setType("agent-decision-ack");
        responseMessage.setContent("决策已接收");
        session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
    }

    private void handleAgentAutomaticDecision(WebSocketSession session, Message msg) throws IOException {
        String advice = (String) msg.getData().get("advice");

        // 处理自动决策
        System.out.println("自动决策建议: " + advice);

        // 发送确认消息
        Message responseMessage = new Message();
        responseMessage.setType("agent-automatic-decision-ack");
        responseMessage.setContent("自动决策已接收");
        session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
    }

    // 在 handleTextMessage 中新增消息类型处理
    private void handleAutoDialogue(WebSocketSession session, Message msg) throws IOException {
        String simulationId = msg.getSimulationId();
        String dialogueContent = (String) msg.getData().get("content");

        // 1. 处理自动对话内容
        System.out.println("收到自动对话内容: " + dialogueContent);

        // 2. 构建 WebSocket 响应消息
        Message responseMessage = new Message();
        responseMessage.setType("auto-dialogue-response");
        responseMessage.setSimulationId(simulationId);
        responseMessage.setContent("自动对话已处理");
        responseMessage.setData(Map.of(
                "content", dialogueContent
        ));

        // 3. 发送响应消息
        session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
    }
    public void broadcastMessage(Message message) {
        try {
            String jsonMessage = mapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(jsonMessage);

            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("广播消息失败: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session); // 注册新会话
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId()); // 注销会话
    }
}
