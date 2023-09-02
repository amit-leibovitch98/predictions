package simulation;


import jdk.nashorn.internal.ir.Block;
import simulation.utils.expression.CondExpression;
import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation implements Runnable {
    private World world;
    private int tick;
    private final String guid;

    private Map<String, Integer> retrivedEntitiesPopulation;
    private Map<String, Object> retrivedEnvVarsValues;


    public Simulation(World world) {
        this.world = world;
        this.guid = UUID.randomUUID().toString().substring(0, 8);
        this.tick = 0;
        this.retrivedEntitiesPopulation = null;
        this.retrivedEnvVarsValues = null;
    }

    public Map<String, Integer> getRetrivedEntitiesPopulation() {
        return retrivedEntitiesPopulation;
    }

    public Map<String, Object> getRetrivedEnvVarsValues() {
        return retrivedEnvVarsValues;
    }

    public void init() {
        if (retrivedEntitiesPopulation == null || retrivedEnvVarsValues == null) {
            throw new IllegalStateException("can't init- retrievedEntitiesPopulation or retrievedEnvVarsValues is null");
        }
        //init envVars
        for (EnvironmentVariable envVar : world.getEnvironmentVars()) {
            envVar.setValue(retrivedEnvVarsValues.get(envVar.getName()));
        }

        //init entities
        for (Entity entity : world.getEntities()) {
            entity.setPopulation(retrivedEntitiesPopulation.get(entity.getName()));
        }

        //update condExpressions
        CondExpression.updateEnvVars(world.getEnvironmentVars());
        //create entity instances
        world.initPopulation();

    }


    public void start() {
        init();
        long startTimeNano = System.nanoTime(), elapsedTimeNano = 0;
        float elapsedTimeSecs = 0;
        int maxTicks = world.getTerminationConds().getByTicks();
        int maxTime = world.getTerminationConds().getByTime();
        while (tick < maxTicks /*&& elapsedTimeSecs < maxTime*/) {
            for (EntityInstance entityInstance : world.getEntityInstances()) {
                if (entityInstance.isAlive()) {
                    try {
                        for (Rule rule : world.getRules()) {
                            try {
                                rule.activateRule(entityInstance, tick);
                            } catch (Exception e) {
                                System.out.println("Rule " + rule.getName() + " failed to activate: " + e.getMessage() + " --> Simulation failed.");
                                break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            tick++;
            elapsedTimeNano = System.nanoTime() - startTimeNano;
            elapsedTimeSecs = elapsedTimeNano / 1000000000f;
        }
    }

    public String getGuid() {
        return guid;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void run() {
        start();
        SimulationManager.getInstance().saveResults(this);
    }

    public void setRetrivedData(Map<String, Integer> entitiesPopulation, Map<String, Object> envVarsVals) {
        this.retrivedEntitiesPopulation = entitiesPopulation;
        this.retrivedEnvVarsValues = envVarsVals;
    }
}
