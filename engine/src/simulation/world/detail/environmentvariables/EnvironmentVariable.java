package simulation.world.detail.environmentvariables;

import simulation.utils.Range;
import simulation.utils.Type;

public class EnvironmentVariable {
    private final String name;
    private final Range range;
    private final Type type;
    private Object value;

    public EnvironmentVariable(String name, Range range, Type type) {
        this.name = name;
        this.range = range;
        this.type = type;
        this.value = range.getFrom();
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

    public Object getValue() {
        return value;
    }

}
