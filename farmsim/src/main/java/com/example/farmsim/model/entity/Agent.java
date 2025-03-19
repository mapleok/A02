package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@Setter
public class Agent {
    @Id
    private String agentId;
    private String agentName;
    private String roleType;
    private String currentAction;
    private String name;

    @ElementCollection
    @CollectionTable(name = "agent_properties", joinColumns = @JoinColumn(name = "agent_id"))
    @MapKeyColumn(name = "property_key")
    @Column(name = "property_value")
    private Map<String, String> properties;

    // 关联的模拟
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "simulation_id") // 确保列名与数据库一致
    private Simulation simulation;

}