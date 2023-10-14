package simulation;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationDC {
    private final Simulation simulation;
    private int primeryEntityCount;
    private int seconderyEntityCount;
    private IntegerProperty tick;
    private Map<Integer, Pair<Integer, Integer>> populationOnEachTick;

    public SimulationDC(Simulation simulation) {
        this.populationOnEachTick = new LinkedHashMap<>();
        this.simulation = simulation;
        this.tick = simulation.getTick();
        this.primeryEntityCount = simulation.getPrimeryEntityInitPopulation();
        this.seconderyEntityCount = (simulation.getSecendaryEntityInitPopulation());
        this.tick.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(
                    () -> {
                        this.primeryEntityCount = simulation.getWorld().countAliveOfEntity(simulation.getWorld().getEntities().get(0));
                        this.seconderyEntityCount = simulation.getWorld().countAliveOfEntity(simulation.getWorld().getEntities().get(1));
                    }
            );
            this.populationOnEachTick.put((int) newValue, new Pair<>(this.primeryEntityCount, this.seconderyEntityCount));
        });
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public int getPrimeryEntityCount() {
        return primeryEntityCount;
    }

    public int getSeconderyEntityCount() {
        return seconderyEntityCount;
    }

    public String getMassege() {
        return this.simulation.getMassege();
    }

    public BooleanProperty getIsRunning() {
        return simulation.getIsRunning();
    }

    public Map<Integer,Pair<Integer, Integer>> getPopulationTickMap() {
        return this.populationOnEachTick;
    }
}
