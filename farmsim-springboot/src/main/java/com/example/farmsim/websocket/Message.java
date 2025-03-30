package com.example.farmsim.websocket;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Message {
    private String type;
    private String content;
    private Map<String, Object> data;
    private String simulationId;
    private String timestamp = LocalDateTime.now().toString();

    private String animationType; // 动画类型标识
    private String commandId;    // 指令唯一ID
    private Map<String, Object> animationParams;

    public static Message buildActionMessage(String type, Map<String,Object> params) {
        Message msg = new Message();
        msg.setType("agent-action");
        msg.setAnimationType(type);
        msg.setAnimationParams(params);
        msg.setCommandId(UUID.randomUUID().toString());
        return msg;
    }// 动画参数

}