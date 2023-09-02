package simulation;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simulation.world.detail.entity.Entity;
import threads.ThreadQueue;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.locks.Lock;

public class SimulationManager extends Observable {
    private static SimulationManager instance;
    private static SimpleListProperty<Simulation> simulations;
    private static ObservableList<String> simulationGuids;
    private ThreadQueue threadQueue;

    private SimulationManager() {
        this.simulations = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.simulationGuids = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public static SimulationManager getInstance() {
        if (instance == null) {
            instance = new SimulationManager();
        }
        return instance;
    }

    public void setThreadQueue(int threadQueueSize) {
        this.threadQueue = new ThreadQueue(threadQueueSize);
    }

    public ThreadQueue getThreadQueue() {
        return this.threadQueue;
    }

    public void saveResults(Simulation simulation) {
        Lock lock = threadQueue.getLock();
        lock.lock();
        simulations.add(simulation);
        Platform.runLater(() -> {
            // Perform UI updates here, such as adding items to the ListView
            simulationGuids.add(simulation.getGuid());
        });
        lock.unlock();
    }

    public SimpleListProperty<Simulation> getSimulations() {
        return simulations;
    }


    public Simulation getSimulationByGuid(String guid) {
        for (Simulation simulation : simulations) {
            if (simulation.getGuid().equals(guid)) {
                return simulation;
            }
        }
        return null;
    }

    public String getSimulationResultByEntities(String guid, String entityName) {
        Simulation simulation = getSimulationByGuid(guid);
        for (Entity entity : simulation.getWorld().getEntities()) {
            if (entity.getName().equals(entityName)) {
                return "Population at the begining: " + entity.getPopulation() + "\n" +
                        "Population at the end: " + simulation.getWorld().countAliveOfEntity(entity);
            }
        }
        return null;
    }

    public Map<Object, Integer> getSimulationResultByHistogram(String guid, String entityName, String propertyName) {
        Simulation simulation = getSimulationByGuid(guid);
        try {
            Entity entity = simulation.getWorld().getEntityByName(entityName);
            Map<Object, Integer> histogram = entity.getProperty(propertyName).getHistogram(simulation.getWorld().getEntityInstances());
            return histogram;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<String> getEntitiesNamesForSimulation(String guid) {
        List<String> entitiesNames = new ArrayList<String>();
        Simulation simulation = getSimulationByGuid(guid);
        simulation.getWorld().getEntities().stream().
                forEach(entity -> entitiesNames.add(entity.getName()));
        return entitiesNames;
    }

    public List<String> getEntityPropertiesNamesForSimulation(String guid, String entityName) {
        List<String> propertiesNames = new ArrayList<String>();
        Simulation simulation = getSimulationByGuid(guid);
        try {
            Entity entity = simulation.getWorld().getEntityByName(entityName);
            entity.getProperties().stream().
                    forEach(property -> propertiesNames.add(property.getName()));
            return propertiesNames;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return propertiesNames;
    }


    public ObservableList<String> getSimulationGuids() {
        return simulationGuids;
    }

}
