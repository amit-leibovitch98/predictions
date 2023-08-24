package simulation;

import simulation.world.detail.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulationManager {
    private static SimulationManager instance;
    private static List<Simulation> simulations;



    private SimulationManager() {
        this.simulations = new ArrayList<Simulation>();
    }

    public static SimulationManager getInstance() {
        if (instance == null) {
            instance = new SimulationManager();
        }
        return instance;
    }

    public void saveResults(Simulation simulation) {
        simulations.add(simulation);
    }

    public List<Simulation> showSimulations() {
        return simulations;
    }

    private Simulation getSimulationByGuid(String guid) {
        for(Simulation simulation : simulations) {
            if(simulation.getGuid().equals(guid)) {
                return simulation;
            }
        }
        return null;
    }

    public void getSimulationResultByEntities(String guid) {
        Simulation simulation = getSimulationByGuid(guid);
        for(Entity entity: simulation.getWorld().getEntities()) {
            System.out.println("•" + entity.getName() + ":");
            System.out.println(" population at the begining: " + entity.getPopulation());
            System.out.println(" population at the end: " + simulation.getWorld().countAliveOfEntity(entity));
        }
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

}
