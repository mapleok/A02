package com.example.farmsim.model.dto;// AgentDialogueDTO.java
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDialogueDTO {
    private Long id;
    private String agentId;
    private String roleType;
    private String content;
    private LocalDateTime timestamp;
}