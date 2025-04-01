// Message.java
package com.example.farmsim.websocket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
public class Message {
    private String type;
    private String content;
    private Map<String, Object> data;
    private String simulationId;
    private String timestamp;

    // 添加全参构造方法（Lombok @Builder 需要）
    public Message(String type, String content, Map<String, Object> data, String simulationId, String timestamp) {
        this.type = type;
        this.content = content;
        this.data = data;
        this.simulationId = simulationId;
        this.timestamp = timestamp;
    }

    // 添加无参构造方法（可选，某些框架需要）
    public Message() {}
}