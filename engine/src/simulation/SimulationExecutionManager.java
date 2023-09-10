package simulation;

import threads.ThreadQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class SimulationExecutionManager {
    private ThreadQueue threadQueue;
    private Map<String, SimulationDC> simulationDCs;

    public SimulationExecutionManager() {
        this.simulationDCs = new HashMap<>();
    }
    public void setThreadQueue(int threadQueueSize) {
        this.threadQueue = new ThreadQueue(threadQueueSize);
    }

    public SimulationDC addSimulation(Simulation simulation) {
        SimulationDC simulationDC = new SimulationDC(simulation);
        simulationDCs.put(simulation.getGuid(), simulationDC);
        threadQueue.addTask(simulation);
        return simulationDC;
    }

    public Lock getLock() {
        return threadQueue.getLock();
    }

    public int getTherPoolSize() {
        return threadQueue.getMaxThearsNum();
    }

    public SimulationDC getSimulationDC(String guid) {
        return simulationDCs.get(guid);
    }
}
