// AgentDialogue.java
package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class AgentDialogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String agentId;
    @Column(name = "role_type")
    private String roleType;
    @Column(length = 2000)
    private String content;
    private LocalDateTime timestamp;

    // AgentDialogue.java 实体类新增字段
    @Column(name = "dialogue_round")
    private int dialogueRound; // 标记当前是第几轮对话


    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;

}