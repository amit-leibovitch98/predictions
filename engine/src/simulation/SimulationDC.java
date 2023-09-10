package simulation;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimulationDC {
    private StringProperty massege;
    private final Simulation simulation;
    private IntegerProperty primeryEntityCount;
    private IntegerProperty seconderyEntityCount;
    private IntegerProperty tick;
    public SimulationDC(Simulation simulation) {
        this.massege = simulation.getMassege();
        this.simulation = simulation;
        this.tick = simulation.getTick();
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

}
