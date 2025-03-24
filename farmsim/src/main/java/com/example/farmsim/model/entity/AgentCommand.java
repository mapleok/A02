package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AgentCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;
    private String target;
    private String parameters; // 存储JSON字符串
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private CommandStatus status = CommandStatus.PENDING; // 执行状态

    private LocalDateTime executedTime; // 执行时间戳


    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;
}