package simulation.world.detail;

import com.sun.istack.internal.Nullable;

import java.util.Optional;

public class TerminationCond {
    private Integer byTicks;
    private Integer byTime;

    public TerminationCond(Integer byTicks, Integer byTime) {
        this.byTicks = byTicks;
        this.byTime = byTime;
    }

    public Integer getByTicks() {
        return byTicks;
    }
    public Integer getByTime() {
        return byTime;
    }
}
