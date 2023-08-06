package simulation.world.detail.environmentvariables;

import simulation.utils.Range;
import simulation.utils.Type;

public class EnvironmentVariable {
    private final String name;
    private final Range range;
    private final Type type;

    public EnvironmentVariable(String name, Range range, Type type) {
        this.name = name;
        this.range = range;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Range getRange() {
        return range;
    }

    public Type getType() {
        return type;
    }

}
