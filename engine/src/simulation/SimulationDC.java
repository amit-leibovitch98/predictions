package simulation;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;

public class SimulationDC {
    private StringProperty massege;
    private final Simulation simulation;
    private IntegerProperty primeryEntityCount;
    private IntegerProperty seconderyEntityCount;
    private IntegerProperty tick;

    public SimulationDC(Simulation simulation) {
        this.primeryEntityCount = new SimpleIntegerProperty();
        this.seconderyEntityCount = new SimpleIntegerProperty();
        this.simulation = simulation;
        this.massege = this.simulation.getMassege();
        this.tick = simulation.getTick();
        this.primeryEntityCount.setValue(simulation.getPrimeryEntityInitPopulation());
        this.seconderyEntityCount.setValue((simulation.getSecendaryEntityInitPopulation()));
        this.tick.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(
                    () -> {
                        this.primeryEntityCount.setValue(simulation.getWorld().countAliveOfEntity(simulation.getWorld().getEntities().get(0)));
                        this.seconderyEntityCount.setValue(simulation.getWorld().countAliveOfEntity(simulation.getWorld().getEntities().get(1)));
                    }
            );
        });
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public IntegerProperty getPrimeryEntityCount() {
        return primeryEntityCount;
    }

    public IntegerProperty getSeconderyEntityCount() {
        return seconderyEntityCount;
    }

    public StringProperty getMassege() {
        return this.massege;
    }

    public BooleanProperty getIsRunning() {
        return simulation.getIsRunning();
    }
}
