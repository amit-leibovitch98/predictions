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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Semaphore;

public class Simulation implements Runnable, Cloneable {
    private String massege;
    private World world;
    private IntegerProperty tick;
    private final String guid;
    private Map<String, Integer> retrivedEntitiesPopulation;
    private Map<String, Object> retrivedEnvVarsValues;
    private BooleanProperty isRunning;
    private AtomicBoolean isPaused;
    private AtomicBoolean isStopped;
    private float currTime;
    private final Semaphore pauseLock = new Semaphore(1);
    private ExecutorService managerThread;

    public Simulation(World world) {
        this.isStopped = new AtomicBoolean(false);
        this.isPaused = new AtomicBoolean(false);
        this.isRunning = new SimpleBooleanProperty();
        Platform.runLater(() -> this.isRunning.set(false));
        this.massege = "Queueing...";
        this.world = world;
        this.guid = UUID.randomUUID().toString().substring(0, 8);
        this.tick = new SimpleIntegerProperty(0);
        this.currTime = 0;
        managerThread = Executors.newSingleThreadExecutor();
    }

    public void init() {
        this.massege = "Retrieving data...";
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
        this.massege = "Initializing population...";
        world.initPopulation(this);
        world.updateActions(world);
    }

    @Override
    public void run() {
        Platform.runLater(() -> this.isRunning.set(true));
        managerThread.execute(this::start);
        this.massege = "Saving results...";
        SimulationManager.getInstance().saveResults(this);
        this.massege = "Finished!";
        Platform.runLater(() -> this.isRunning.set(false));
    }

    public void start() {
        init();
        long startTimeNano = System.nanoTime(), elapsedTimeNano = 0;
        float elapsedTimeSecs = 0;
        while (checkIfSimulationIsFinished(elapsedTimeSecs, tick.getValue())) {
            synchronized (pauseLock) {
                if (isPaused.get()) {
                    try {
                        pauseLock.wait(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
            }
            this.massege = "Running...";
            moveAllInstances();
            moveOneTick(world.getPrimeryEntityInstances());
            moveOneTick(world.getSeconderyEntityInstances());
            tick.setValue(tick.getValue() + 1);
            elapsedTimeNano = System.nanoTime() - startTimeNano;
            elapsedTimeSecs = elapsedTimeNano / 1000000000f;
            this.currTime = elapsedTimeSecs;
        }
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
                            System.out.println("Rule " + rule.getName() + " failed to activate: " + e.getMessage() + " --> Simulation failed on tick " + tick.getValue() + ".");
                            System.exit(1);
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    private boolean checkIfSimulationIsFinished(float currTime, int tick) {
        if (this.world.getTerminationConds().getByTime() != null && this.world.getTerminationConds().getByTime() >= currTime) {
            return true;
        } else if (this.world.getTerminationConds().getByTicks() != null && this.world.getTerminationConds().getByTicks() >= tick) {
            return true;
        } else if (this.world.getTerminationConds().isInteractive() && !this.isStopped.get()) {
            return true;
        } else {
            return false;
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

    protected String getMassege() {
        return massege;
    }

    public BooleanProperty getIsRunning() {
        return isRunning;
    }

    public synchronized void pauseSimulation() {
        isPaused.set(true);
        this.massege = "Paused";
    }

    public synchronized void resumeSimulation() {
        isPaused.set(false);
    }

    public synchronized void stopSimulation() {
        this.massege = "Stopped";
        Platform.runLater(() -> this.isRunning.setValue(false));
        isStopped.set(true);
    }

    @Override
    public Simulation clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Simulation simulation = new Simulation(world.clone());
        simulation.setRetrivedData(retrivedEntitiesPopulation, retrivedEnvVarsValues);
        return simulation;
    }

    public Float getcurrTime() {
        return currTime;
    }

    public Semaphore getPauseLock() {
        return pauseLock;
    }
}
