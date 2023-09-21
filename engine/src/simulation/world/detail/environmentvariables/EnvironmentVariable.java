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
        } else {
            setValue(null);
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
        if(value != null) {
            switch (type) {
                case DECIMAL:
                    try {
                        this.value = Integer.parseInt(value.toString());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid value for decimal type: " + value);
                    }
                    break;
                case FLOAT:
                    try {
                        this.value = Float.parseFloat(value.toString());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid value for float type: " + value);
                    }
                    break;
                case STRING:
                    if (value != null) {
                        this.value = value.toString();
                    } else {
                        value = "";
                    }
                    break;
                case BOOLEAN:
                    try {
                        if (value != null) {
                            if(value instanceof Boolean) {
                                this.value = value;
                                return;
                            }
                            if (value.equals("false")) {
                                this.value = false;
                            } else if (value.equals("true")) {
                                this.value = true;
                            } else {
                                throw new IllegalArgumentException("Invalid value for boolean type: " + value);
                            }
                        } else {
                            value = false;
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid value for boolean type: " + value);
                    }
                    break;
            }
        }
    }

    public boolean isValid(String val) {
        if (val != null) {
            switch (type) {
                case DECIMAL:
                    try {
                        int intVal = Integer.parseInt(val);
                        return intVal >= range.getFrom() && intVal <= range.getTo();
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case FLOAT:
                    try {
                        float floatVal = Float.parseFloat(val);
                        return floatVal >= range.getFrom() && floatVal <= range.getTo();
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case STRING:
                    return val instanceof String;
                case BOOLEAN:
                    if (val.equals("true")) {
                        return true;
                    } else if (val.equals("false")) {
                        return true;
                    } else {
                        throw new IllegalArgumentException("Invalid value for boolean type: " + val);
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
