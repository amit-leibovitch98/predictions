package simulation;


import simulation.utils.expression.CondExpression;
import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.Rule;

import java.util.List;
import java.util.UUID;

public class Simulation {
    private World world;
    private int tick;
    private final String guid;
    public Simulation (World world) {
        this.world = world;
        this.guid = UUID.randomUUID().toString().substring(0, 8);
        this.tick = 0;
    }
    public void init(){
        world.initPopulation();
        CondExpression.updateEnvVars(world.getEnvironmentVars());
    }
    public Simulation start(){
        long startTimeNano = System.nanoTime(), elapsedTimeNano = 0;
        float elapsedTimeSecs = 0;
        int maxTicks = world.getTerminationConds().getByTicks();
        int maxTime = world.getTerminationConds().getByTime();
        while(tick < maxTicks /*&& elapsedTimeSecs < maxTime*/) {
            for(EntityInstance entityInstance : world.getEntityInstances()) {
                if (entityInstance.isAlive()) {
                    try {
                        for (Rule rule : world.getRules()) {
                            try {
                                rule.activateRule(entityInstance, tick);
                            } catch (Exception e) {
                                System.out.println("Rule " + rule.getName() + " failed to activate: " + e.getMessage() + " --> Simulation failed.");
                                System.out.println(e.getMessage());
                                System.exit(1);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            tick++;
            elapsedTimeNano  = System.nanoTime() - startTimeNano;
            elapsedTimeSecs = elapsedTimeNano / 1000000000f;
        }
        return this;
    }

    public String getGuid() {
        return guid;
    }

    public World getWorld() {
        return world;
    }
}
