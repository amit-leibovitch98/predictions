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
        setValue(range.getFrom());
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
            case DECIMAL:
                try {
                    if(value instanceof Float) {
                        this.value = Math.round((float) value);
                    } else {
                        this.value = value;
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid value for decimal type: " + value);
                }
                break;
            case FLOAT:
                try {
                    this.value = value;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid value for float type: " + value);
                }
                break;
            case STRING:
                this.value = value.toString();
                break;
            case BOOLEAN:
                try {
                    this.value = (boolean) value;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid value for boolean type: " + value);
                }
                break;
        }
    }

    public boolean isValid(Object val) {
        if (val != null) {
            switch (type) {
                case DECIMAL:
                    try {
                        int intVal = (int) val;
                        return intVal >= range.getFrom() && intVal <= range.getTo();
                    } catch (NumberFormatException e) {
                        return false;
                    }
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
            case DECIMAL:
                return (int) (Math.random() * ((int) range.getTo() - (int) range.getFrom() + 1)) + (int) range.getFrom();
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
        return "Environment Variable name: " + name + "\n" +
                " • Type: " + type.toString() + "\n" +
                " • Range: " + range.toString() + "\n" +
                " • Value: " + value.toString();
    }
}
