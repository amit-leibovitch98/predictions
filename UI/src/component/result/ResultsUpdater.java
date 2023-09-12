package component.result;

import component.result.tab.ResultTabController;
import javafx.application.Platform;
import simulation.SimulationDC;

public class ResultsUpdater implements Runnable {
    private final ResultTabController resultTabController;
    private final SimulationDC simulationDC;
    private final Object lock = new Object();
    private boolean conditionMet = false;


    public ResultsUpdater(ResultTabController resultTabController, SimulationDC simulationDC) {
        this.resultTabController = resultTabController;
        this.simulationDC = simulationDC;
    }

    @Override
    public void run() {
        // Your task logic here

        // Wait until the condition is met
        synchronized (lock) {
            while (!conditionMet) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        // Sleep for 200ms
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // Execute your update logic after 200ms
        Platform.runLater(() -> {
            resultTabController.update(
                    simulationDC.getMassege().get(),
                    simulationDC.getPrimeryEntityCount().get(),
                    simulationDC.getSeconderyEntityCount().get()
            );
        });
    }

    public void setConditionMet() {
        // Set the condition to true and notify the waiting thread
        synchronized (lock) {
            conditionMet = true;
            lock.notify();
        }
    }
}
