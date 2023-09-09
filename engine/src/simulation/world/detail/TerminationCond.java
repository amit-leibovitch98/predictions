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
        StringBuilder description = new StringBuilder();

        if (byTicks != null) {
            description.append("The Simulation will terminate after ").append(byTicks).append(" ticks.\n");
        } else {
            description.append("Termination by ticks number is not applicable\n");
        }

        if (byTime != null) {
            description.append("The Simulation will terminate after ").append(byTime).append(" seconds.\n");
        } else {
            description.append("Termination by time number is not applicable\n");
        }

        description.append(isInteractive ? "Interactive mode is enabled." : "Interactive mode is disabled.");

        return description.toString();
    }

    public String getName() {
        return "Termination Condition";
    }

    public boolean isInteractive() {
        return isInteractive;
    }
}
