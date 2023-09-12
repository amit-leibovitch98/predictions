package simulation;


import javafx.application.Platform;
import javafx.beans.property.*;
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
    private StringProperty massege;
    private World world;
    private IntegerProperty tick;
    private final String guid;
    private Map<String, Integer> retrivedEntitiesPopulation;
    private Map<String, Object> retrivedEnvVarsValues;
    private BooleanProperty isRunning;
    private boolean isPaused;
    private boolean isStopped;

    public Simulation(World world) {
        this.isStopped = false;
        this.isPaused = false;
        this.isRunning = new SimpleBooleanProperty();
        Platform.runLater(() -> this.isRunning.setValue(false));
        this.massege = new SimpleStringProperty();
        Platform.runLater(() -> this.massege.setValue("Queueing..."));
        this.world = world;
        this.guid = UUID.randomUUID().toString().substring(0, 8);
        this.tick = new SimpleIntegerProperty(0);
    }

    public void init() {
        Platform.runLater(() -> this.massege.setValue("Retrieving data..."));

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
        Platform.runLater(() -> this.massege.setValue("Initializing population..."));
        world.initPopulation(this);
        world.updateActions(world);
        //todelete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Platform.runLater(() -> this.isRunning.setValue(true));
        start();
        Platform.runLater(() -> this.massege.setValue("Saving results..."));
        SimulationManager.getInstance().saveResults(this);
        Platform.runLater(() -> this.massege.setValue("Finished!"));
        isRunning.setValue(false);
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
            maxTicks = 100; //todelete
        }
        while (tick.getValue() < maxTicks /*&& elapsedTimeSecs < maxTime*/) {
            synchronized (SimulationManager.getInstance().getSimulationExecutionManager().getLock()) {
                while (isPaused) {
                    try {
                        SimulationManager.getInstance().getSimulationExecutionManager().getLock().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (isStopped) {
                    break;
                }
            }
            Platform.runLater(() -> this.massege.setValue("Running... (" + tick.getValue() + " ticks)"));
            moveAllInstances();
            moveOneTick(world.getPrimeryEntityInstances());
            moveOneTick(world.getSeconderyEntityInstances());
            tick.setValue(tick.getValue() + 1);
            elapsedTimeNano = System.nanoTime() - startTimeNano;
            elapsedTimeSecs = elapsedTimeNano / 1000000000f;
            //todelete
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Simulation finished");
    }

    public int getPrimeryEntityInitPopulation() {
        return retrivedEntitiesPopulation.get(this.getWorld().getEntities().get(0).getName());
    }

    public int getSecendaryEntityInitPopulation() {
        return retrivedEntitiesPopulation.get(this.getWorld().getEntities().get(1).getName());
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
                            e.printStackTrace();
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

    protected StringProperty getMassege() {
        return massege;
    }

    public BooleanProperty getIsRunning() {
        return isRunning;
    }

    public synchronized void pauseSimulation() {
        isPaused = true;
        Platform.runLater(() -> this.massege.setValue("Paused (in tick " + tick.getValue() + ")"));
    }

    public synchronized void resumeSimulation() {
        isPaused = false;
        Platform.runLater(() -> this.massege.setValue("Running... (" + tick.getValue() + " ticks)"));
        synchronized (SimulationManager.getInstance().getSimulationExecutionManager().getLock()) {
            SimulationManager.getInstance().getSimulationExecutionManager().getLock().notify(); // Notify the thread to resume
        }
    }


    public synchronized void stopSimulation() {
        Platform.runLater(() -> this.isRunning.setValue(false));
        Platform.runLater(() -> this.massege.setValue("Stopped (in tick " + tick.getValue() + ")"));
        isStopped = true;
    }
}
