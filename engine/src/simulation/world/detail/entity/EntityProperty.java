package simulation.world.detail.entity;

import simulation.utils.Range;
import simulation.utils.Type;
import simulation.utils.Value;

public class EntityProperty {
    private final String name;
    private final Range range;
    private final Type type;
    private Object initialValue;

    public EntityProperty(String name, Range range, Type type, Object initialValue) {
        this.name = name;
        this.range = range;
        this.type = type;
        this.initialValue = initialValue;
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
    public Object getInitialValue() {
        return initialValue;
    }


}
