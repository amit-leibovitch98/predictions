package simulation;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simulation.utils.Type;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityProperty;

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
    private SimulationExecutionManager simulationExecutionManager;

    private SimulationManager() {
        this.simulationExecutionManager = new SimulationExecutionManager();
        this.simulations = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.simulationGuids = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public static SimulationManager getInstance() {
        if (instance == null) {
            instance = new SimulationManager();
        }
        return instance;
    }

    public SimulationExecutionManager getSimulationExecutionManager() {
        return simulationExecutionManager;
    }

    public void saveResults(Simulation simulation) {
        Lock lock = simulationExecutionManager.getLock();
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
        StringBuilder result = new StringBuilder();
        for (Entity entity : simulation.getWorld().getEntities()) {
            if (entity.getName().equals(entityName)) {
                for(EntityProperty prop: entity.getProperties()) {
                    if(prop.getType() == Type.DECIMAL || prop.getType() == Type.FLOAT) {
                        result.append("Average of property").append(prop.getName()).append(": ").append(simulation.getWorld().getAverageProperty(simulation, entity, prop)).append("\n");
                    }
                }
                result.append("Population at the beginning: ").append(entity.getPopulation()).append("\n");
                result.append("Population at the end: ").append(simulation.getWorld().countAliveOfEntity(entity)).append("\n");
            }
        }
        return null;
    }


    public Map<Object, Integer> getSimulationResultByHistogram(String guid, String entityName, String propertyName) {
        Simulation simulation = getSimulationByGuid(guid);
        try {
            Entity entity = simulation.getWorld().getEntityByName(entityName);
            Map<Object, Integer> histogram = entity.getProperty(propertyName).getHistogram(simulation.getWorld().getEntityInstancesByName(entityName));
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
