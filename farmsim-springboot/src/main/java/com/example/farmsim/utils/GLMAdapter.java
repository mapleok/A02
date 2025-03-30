package com.example.farmsim.utils;

import com.example.farmsim.model.entity.Agent;
import com.example.farmsim.model.entity.Environment;
import com.example.farmsim.model.entity.Crop;

public class GLMAdapter {
    public static String buildPrompt(Agent agent, Environment env, Crop crop) {
        return String.format("作为%s，当前温度%.1f℃，土壤肥力%.0f%%，请决定下一步操作。",
                agent.getRoleType(), env.getTemperature(), env.getSoilFertility() * 100);
    }
}