package com.example.farmsim.websocket;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Message {
    private String type;
    private String content;
    private Map<String, Object> data;
    private String simulationId;

}