package com.example.farmsim.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

import com.example.farmsim.service.GLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, WebSocketSession> subscribedSessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private GLMService glmService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        System.out.println("WebSocket 连接已建立");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        subscribedSessions.remove(session.getId());
        System.out.println("WebSocket 连接已关闭");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // 解析消息
            String payload = message.getPayload();
            System.out.println("收到消息: " + payload);
            Message msg = mapper.readValue(payload, Message.class);
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
                case "auto-dialogue":
                    handleAutoDialogue(session, msg);
                    break;
                case "animation-ack":
                    handleAnimationAck(session, msg);
                    break;
                case "subscribe":
                    handleSubscribe(session, msg);
                    break;
                default:
                    session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"未知消息类型: " + type + "\"}"));
                    break;
            }
        } catch (Exception e) {
            System.err.println("处理消息失败: " + e.getMessage());
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"服务器错误: " + e.getMessage() + "\"}"));
            } catch (IOException ex) {
                System.err.println("发送错误消息失败: " + ex.getMessage());
            }
        }
    }

    private void handleStartDialogue(WebSocketSession session, Message msg) {
        try {
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
        } catch (Exception e) {
            System.err.println("处理 start-dialogue 消息失败: " + e.getMessage());
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"处理 start-dialogue 消息失败: " + e.getMessage() + "\"}"));
            } catch (IOException ex) {
                System.err.println("发送错误消息失败: " + ex.getMessage());
            }
        }
    }

    private void handleAgentDecision(WebSocketSession session, Message msg) {
        try {
            String agentId = (String) msg.getData().get("agentId");
            String decision = (String) msg.getData().get("decision");

            // 处理 Agent 决策
            System.out.println("Agent " + agentId + " 的决策: " + decision);

            // 发送确认消息
            Message responseMessage = new Message();
            responseMessage.setType("agent-decision-ack");
            responseMessage.setContent("决策已接收");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
        } catch (Exception e) {
            System.err.println("处理 agent-decision 消息失败: " + e.getMessage());
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"处理 agent-decision 消息失败: " + e.getMessage() + "\"}"));
            } catch (IOException ex) {
                System.err.println("发送错误消息失败: " + ex.getMessage());
            }
        }
    }

    private void handleAgentAutomaticDecision(WebSocketSession session, Message msg) {
        try {
            String advice = (String) msg.getData().get("advice");

            // 处理自动决策
            System.out.println("自动决策建议: " + advice);

            // 发送确认消息
            Message responseMessage = new Message();
            responseMessage.setType("agent-automatic-decision-ack");
            responseMessage.setContent("自动决策已接收");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
        } catch (Exception e) {
            System.err.println("处理 agent-automatic-decision 消息失败: " + e.getMessage());
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"处理 agent-automatic-decision 消息失败: " + e.getMessage() + "\"}"));
            } catch (IOException ex) {
                System.err.println("发送错误消息失败: " + ex.getMessage());
            }
        }
    }

    private void handleAutoDialogue(WebSocketSession session, Message msg) {
        try {
            String simulationId = msg.getSimulationId();
            String dialogueContent = (String) msg.getData().get("content");

            // 处理自动对话内容
            System.out.println("收到自动对话内容: " + dialogueContent);

            // 构建 WebSocket 响应消息
            Message responseMessage = new Message();
            responseMessage.setType("auto-dialogue-response");
            responseMessage.setSimulationId(simulationId);
            responseMessage.setContent("自动对话已处理");
            responseMessage.setData(Map.of(
                    "content", dialogueContent
            ));

            // 发送响应消息
            session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
        } catch (Exception e) {
            System.err.println("处理 auto-dialogue 消息失败: " + e.getMessage());
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"处理 auto-dialogue 消息失败: " + e.getMessage() + "\"}"));
            } catch (IOException ex) {
                System.err.println("发送错误消息失败: " + ex.getMessage());
            }
        }
    }

    private void handleAnimationAck(WebSocketSession session, Message msg) {
        String commandId = (String) msg.getData().get("commandId");
        System.out.println("Unity确认执行指令: " + commandId);
    }

    private void handleSubscribe(WebSocketSession session, Message msg) {
        try {
            Map<String, Object> data = msg.getData();
            String channel = (String) data.get("channel");

            if ("dialogue".equals(channel)) {
                // 订阅对话通道
                subscribedSessions.put(session.getId(), session);
                session.sendMessage(new TextMessage("{\"type\":\"subscribe-ack\", \"content\":\"已订阅对话通道\"}"));
            } else {
                session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"未知订阅通道: " + channel + "\"}"));
            }
        } catch (IOException e) {
            System.err.println("处理 subscribe 消息失败: " + e.getMessage());
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("{\"type\":\"error\", \"content\":\"订阅失败: " + e.getMessage() + "\"}"));
            } catch (IOException ex) {
                System.err.println("发送错误消息失败: " + ex.getMessage());
            }
        }
    }

    // WebSocketHandler.java
    public void broadcastMessage(Message message) {
        try {
            // 1. 将消息对象序列化为JSON字符串
            String jsonMessage = mapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(jsonMessage);

            // 2. 调试日志：打印要广播的消息
            System.out.println("[广播消息] " + jsonMessage);

            // 3. 遍历所有活跃的WebSocket会话
            for (WebSocketSession session : sessions.values()) {
                try {
                    if (session.isOpen()) {
                        // 4. 发送消息并记录成功日志
                        session.sendMessage(textMessage);
                        System.out.println("[成功发送] 会话ID: " + session.getId());
                    } else {
                        // 5. 清理已关闭的会话
                        sessions.remove(session.getId());
                        System.out.println("[清理会话] 已关闭的会话: " + session.getId());
                    }
                } catch (IOException e) {
                    // 6. 处理发送失败的情况
                    System.err.println("[发送失败] 会话ID: " + session.getId() + ", 错误: " + e.getMessage());
                    sessions.remove(session.getId()); // 移除故障会话
                }
            }
        } catch (Exception e) {
            // 7. 处理序列化等全局错误
            System.err.println("[广播失败] 消息序列化错误: " + e.getMessage());
        }
    }
}