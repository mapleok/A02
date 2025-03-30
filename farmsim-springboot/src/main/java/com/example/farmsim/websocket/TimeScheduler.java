package com.example.farmsim.websocket;

import com.example.farmsim.model.entity.Simulation;
import com.example.farmsim.model.entity.SimulationStatus;
import com.example.farmsim.repository.SimulationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeScheduler {

    @Autowired
    private SimulationRepo simulationRepo;

    // 每分钟更新一次（实际时间）
    @Scheduled(fixedRate = 60_000)
    public void advanceTime() {
        List<Simulation> runningSimulations = simulationRepo.findByStatus(SimulationStatus.RUNNING);
        for (Simulation sim : runningSimulations) {
            // 根据时间加速倍数更新模拟天数
            sim.setSimulationDays(sim.getSimulationDays() + sim.getTimeScale());
            simulationRepo.save(sim);
        }
    }
}