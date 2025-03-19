package com.example.farmsim.model.entity;

import com.example.farmsim.model.entity.Simulation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AgentDialogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String agentId;
    private String role;
    @Column(length = 2000)
    private String content;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "simulation_id") // 确保列名与数据库一致
    private Simulation simulation;
}