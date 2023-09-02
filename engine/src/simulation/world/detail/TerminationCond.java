package simulation.world.detail;

import com.sun.istack.internal.Nullable;

import java.util.Optional;

public class TerminationCond implements ISimulationComponent {
    private Integer byTicks;
    private Integer byTime;
    private boolean isInteractive;

    public TerminationCond(Integer byTicks, Integer byTime, boolean isInteractive) {
        this.byTicks = byTicks;
        this.byTime = byTime;
        this.isInteractive = isInteractive;
    }

    public Integer getByTicks() {
        return byTicks;
    }
    public Integer getByTime() {
        return byTime;
    }
    public String getInfo() {
        return "The Simulation will terminate after " + byTicks + " ticks.\n" +
                "The Simulation will terminate after " + byTime + " seconds.";
    }
    public String getName() {
        return "Termination Condition";
    }
    public boolean isInteractive() {
        return isInteractive;
    }
}
