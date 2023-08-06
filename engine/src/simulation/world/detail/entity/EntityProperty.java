package simulation.world.detail.entity;

import simulation.utils.Range;
import simulation.utils.Type;
import simulation.utils.Value;

public class EntityProperty {
    private final String name;
    private final Range range;
    private final Value value;
    private final Type type;

    public EntityProperty(String name, Range range, Value value, Type type) {
        this.name = name;
        this.range = range;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Range getRange() {
        return range;
    }

    public Value getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public void setValue(Value value) {
        this.value.setValue(value.getCurrValue());
    }
}
