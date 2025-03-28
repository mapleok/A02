package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Simulation {
    @Id
    private String id;
    private String name;
    private String description;
    private String season;
    private LocalDate startDate;
    private int timeScale;
    private int simulationDays;// 时间流逝速率（1 分钟 = X 天）

    @Enumerated(EnumType.STRING)
    private SimulationStatus status;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL)
    private List<Agent> agents;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL)
    private List<Crop> crops;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL)
    private List<Environment> environments;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL)
    private List<AgentDialogue> dialogues;

    // Simulation.java
    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL)
    private List<AgentCommand> commands; // 新增关联的指令实体

    public Simulation() {}

    @Column(columnDefinition = "TEXT")
    private String latestCommandJson;
    @Column(columnDefinition = "TEXT")
    private String currentPrompt;

}

