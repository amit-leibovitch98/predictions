package facade;



import file.WorldProxy;
import simulation.Simulation;
import simulation.SimulationManager;
import simulation.world.World;
import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.WorldDataCenter;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EngineFacade {
    private Simulation currSimulation;
    private WorldProxy worldProxy;
    private World world;
    private boolean readFileFromPathSuccess = false;


    public void readFile(String path) {
        if(path == null) {
            throw new IllegalArgumentException("path is null");
        } else {
            worldProxy = new WorldProxy(path);
            this.world = worldProxy.getWorld();
            this.readFileFromPathSuccess = true;
        }
    }

    public boolean isReadFileFromPathSuccess() {
        return readFileFromPathSuccess;
    }

    public void showSimulationDetails() {
        WorldDataCenter worldDataCenter = new WorldDataCenter(world);
        System.out.println("---Printing World Details---");
        List<List<List<String>>> worldDeats = worldDataCenter.getWorldDeatils();
        for (List<List<String>> worldProp : worldDeats) {
            for (List<String> strings : worldProp) {
                for (String string : strings) {
                    System.out.println(string);
                }
            }
        }
        System.out.println("---Printing World Details Finished---");
    }

    public List<Entity> getEntities() {
        return this.world.getEntities();
    }

    public List<Rule> getRules() {
        return this.world.getRules();
    }
    public TerminationCond getTerminationCond() {
        return this.world.getTerminationCond();
    }
    public List<ISimulationComponent> getAllSimulationComponents() {
        List<ISimulationComponent> simulationComponents = new ArrayList<>();
        simulationComponents.addAll(this.world.getEntities());
        simulationComponents.addAll(this.world.getEnvironmentVars());
        simulationComponents.addAll(this.world.getRules());
        simulationComponents.add(this.world.getTerminationCond());
        return simulationComponents;

    }

    public void startSimulation(Map<String, Integer> entitiesPopulation, Map<String, Object> envVarsVals) {
        this.currSimulation = new Simulation(world);
        this.currSimulation.setRetrivedData(entitiesPopulation, envVarsVals);
        SimulationManager.getInstance().getThreadQueue().addTask(this.currSimulation);
    }

    public List<Simulation> getPastSimulationResult() {
        SimulationManager simulationManager = SimulationManager.getInstance();
        return simulationManager.getSimulations();
    }

    public List<EnvironmentVariable> getEnvironmentVariables() {
        return this.world.getEnvironmentVars();
    }

    public boolean checkEnvVars(List<Object> envVarsVals) {
        int i = 0;
        for(EnvironmentVariable envVar : this.world.getEnvironmentVars()) {
            if(!envVar.isValid(envVarsVals.get(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public void updateEnvVar(String envVarName, Object value) {
        if (this.world.getEnvironmentVar(envVarName).isValid(value)) {
            this.world.getEnvironmentVar(envVarName).setValue(value);
        }
    }
}
