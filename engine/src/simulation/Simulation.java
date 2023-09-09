package simulation;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import simulation.utils.expression.CondExpression;
import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Simulation implements Runnable {
    private World world;
    private IntegerProperty tick;
    private final String guid;
    private Map<String, Integer> retrivedEntitiesPopulation;
    private Map<String, Object> retrivedEnvVarsValues;

    public Simulation(World world) {
        this.world = world;
        this.guid = UUID.randomUUID().toString().substring(0, 8);
        this.tick = new SimpleIntegerProperty(0);
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
        world.initPopulation(this);
        world.updateActions(world);
    }

    @Override
    public void run() {
        start();
        SimulationManager.getInstance().saveResults(this);
    }

    public void start() {
        init();
        int maxTicks, maxTime;
        long startTimeNano = System.nanoTime(), elapsedTimeNano = 0;
        float elapsedTimeSecs = 0;
        try {
            maxTicks = world.getTerminationConds().getByTicks();
            maxTime = world.getTerminationConds().getByTime();
        } catch (Exception e) {
            maxTicks = 100; //fixme: fix while condition
        }
        while (tick.getValue() < maxTicks /*&& elapsedTimeSecs < maxTime*/) {
            moveAllInstances();
            moveOneTick(world.getPrimeryEntityInstances());
            moveOneTick(world.getSeconderyEntityInstances());
            tick.setValue(tick.getValue() + 1);
            elapsedTimeNano = System.nanoTime() - startTimeNano;
            elapsedTimeSecs = elapsedTimeNano / 1000000000f;
        }
    }

    private void moveAllInstances() {
        for (EntityInstance currEntityInstance : this.world.getPrimeryEntityInstances()) {
            currEntityInstance.move(world.getGrid());
        }
        for (EntityInstance currEntityInstance : this.world.getSeconderyEntityInstances()) {
            currEntityInstance.move(world.getGrid());
        }
    }

    private void moveOneTick(List<EntityInstance> entityInstances) {
        for (EntityInstance primeryEntityInstance : entityInstances) {
            if (primeryEntityInstance.isAlive()) {
                try {
                    for (Rule rule : world.getRules()) {
                        try {
                            rule.activateRule(primeryEntityInstance, tick.getValue(), world.getSeconderyEntityInstances());
                        } catch (Exception e) {
                            System.out.println("Rule " + rule.getName() + " failed to activate: " + e.getMessage() + " --> Simulation failed.");
                            System.exit(1);
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    public IntegerProperty getTick() {
        return tick;
    }

    public String getGuid() {
        return guid;
    }

    public World getWorld() {
        return world;
    }

    public void setRetrivedData(Map<String, Integer> entitiesPopulation, Map<String, Object> envVarsVals) {
        this.retrivedEntitiesPopulation = entitiesPopulation;
        this.retrivedEnvVarsValues = envVarsVals;
    }
}
