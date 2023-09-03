package facade;



import file.WorldProxy;
import simulation.Simulation;
import simulation.SimulationManager;
import simulation.utils.Grid;
import simulation.world.World;
import simulation.world.WorldDef;
import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EngineFacade {
    private WorldProxy worldProxy;
    private WorldDef worldDef;
    private boolean readFileFromPathSuccess = false;


    public void readFile(String path) {
        if(path == null) {
            throw new IllegalArgumentException("path is null");
        } else {
            worldProxy = new WorldProxy(path);
            this.worldDef = worldProxy.getWorld();
            this.readFileFromPathSuccess = true;
        }
    }

    public boolean isReadFileFromPathSuccess() {
        return readFileFromPathSuccess;
    }

    public List<Entity> getEntities() {
        return this.worldDef.getEntities();
    }

    public List<Rule> getRules() {
        return this.worldDef.getRules();
    }
    public TerminationCond getTerminationCond() {
        return this.worldDef.getTerminationCond();
    }
    public List<ISimulationComponent> getAllSimulationComponents() {
        List<ISimulationComponent> simulationComponents = new ArrayList<>();
        simulationComponents.addAll(this.worldDef.getEntities());
        simulationComponents.addAll(this.worldDef.getEnvironmentVars());
        simulationComponents.addAll(this.worldDef.getRules());
        simulationComponents.add(this.worldDef.getTerminationCond());
        return simulationComponents;

    }

    public void startSimulation(Map<String, Integer> entitiesPopulation, Map<String, Object> envVarsVals) {
        Simulation currSimulation = new Simulation(this.worldDef.createWorld());
        currSimulation.setRetrivedData(entitiesPopulation, envVarsVals);
        SimulationManager.getInstance().getThreadQueue().addTask(currSimulation);
    }

    public void rerunSimulation(String guid) {
        SimulationManager.getInstance().getThreadQueue().addTask(SimulationManager.getInstance().getSimulationByGuid(guid));
    }

    public List<EnvironmentVariable> getEnvironmentVariables() {
        return this.worldDef.getEnvironmentVars();
    }

    public Grid getGrid() {
        return this.worldDef.getGrid();
    }
}
