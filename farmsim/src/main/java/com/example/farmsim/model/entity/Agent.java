package com.example.farmsim.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Agent {
    @Id
    @Column(name = "agent_id")
    private String agentId;
    @Column(name = "agent_name")
    private String agentName;
    @Column(name = "role_type")
    private String roleType;
    @Column(name = "current_action")
    private String currentAction;
    @Column(name = "name")
    private String name;

    @ElementCollection
    @CollectionTable(name = "agent_properties", joinColumns = @JoinColumn(name = "agent_id"))
    @MapKeyColumn(name = "property_key")
    @Column(name = "property_value")
    private Map<String, String> properties;
    @Column(nullable = false)
    private double learningAbility;
    @Column(nullable = false)
    private double plantingSkill;
    @Column(nullable = false)
    private double localKnowledge;

    @ElementCollection
    @CollectionTable(name = "agent_accessible_data")
    private List<String> accessibleData;

    // 关联的模拟
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "simulation_id") // 确保列名与数据库一致
    private Simulation simulation;

}