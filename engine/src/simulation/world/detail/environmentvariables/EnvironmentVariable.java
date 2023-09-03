package simulation.world.detail.environmentvariables;

import simulation.utils.Range;
import simulation.utils.Type;
import simulation.world.detail.ISimulationComponent;

public class EnvironmentVariable implements ISimulationComponent {
    private final String name;
    private final Range range;
    private final Type type;
    private Object value;

    public EnvironmentVariable(String name, Range range, Type type) {
        this.name = name;
        this.range = range;
        this.type = type;
        if (range != null) {
            setValue(range.getFrom());
        }
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

    public void setValue(Object value) {
        switch (type) {
            case FLOAT:
                try {
                    if (value instanceof String)
                        this.value = Float.parseFloat((String) value);
                    else if (value instanceof Float)
                        this.value = value;
                    else
                        throw new IllegalArgumentException("Invalid value for float type: " + value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid value for float type: " + value);
                }
                break;
            case STRING:
                this.value = value.toString();
                break;
            case BOOLEAN:
                try {
                    if (value instanceof String)
                        this.value = Boolean.parseBoolean((String) value);
                    else if (value instanceof Boolean)
                        this.value = value;
                    else
                        throw new IllegalArgumentException("Invalid value for boolean type: " + value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid value for boolean type: " + value);
                }
                break;
        }
    }

    public boolean isValid(Object val) {
        if (val != null) {
            switch (type) {
                case FLOAT:
                    try {
                        float floatVal = (float) val;
                        return floatVal >= range.getFrom() && floatVal <= range.getTo();
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case STRING:
                    return val instanceof String;
                case BOOLEAN:
                    try {
                        Boolean.parseBoolean(val.toString());
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                default:
                    return false;
            }
        } else {
            return true;
        }
    }

    public Object initRandomVal() {
        switch (type) {
            case FLOAT:
                return (float) (Math.random() * (range.getTo() - range.getFrom())) + range.getFrom();
            case STRING:
                return "string";
            case BOOLEAN:
                return Math.random() < 0.5;
            default:
                return null;
        }
    }

    public String getInfo() {
        StringBuilder description = new StringBuilder("Environment Variable name: " + name + "\n");
        description.append(" • Type: ").append(type.toString()).append("\n");
        if (range != null) {
            description.append(" • Range: ").append(range).append("\n");
        } else {
            description.append(" • Range: no range\n");
        }
        if (value != null) {
            description.append(" • Value: ").append(value);
        } else {
            description.append(" • Value: no initial value");
        }
        return description.toString();
    }
}
