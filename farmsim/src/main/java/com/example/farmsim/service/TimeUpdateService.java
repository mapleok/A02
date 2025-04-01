package com.example.farmsim.service;

import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.repository.SimulationRepo;
import com.example.farmsim.websocket.Message;
import com.example.farmsim.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TimeUpdateService {

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private WebSocketHandler webSocketHandler;

    // 每秒推送一次时间更新
    @Scheduled(fixedRate = 1000)
    public void pushTimeUpdate() {
        List<Simulation> simulations = simulationRepo.findAll();
        for (Simulation sim : simulations) {
            Message message = new Message();
            message.setType("time-update");
            message.setSimulationId(sim.getId());
            message.setData(Map.of(
                    "days", sim.getSimulationDays(),
                    "status", sim.getStatus().name()
            ));
            webSocketHandler.broadcastMessage(message);
        }
    }
}