package simulation.world.detail.entity;

import simulation.utils.Range;
import simulation.utils.Type;

import java.util.*;

public class EntityProperty {
    private final String name;
    private final Range range;
    private final Type type;
    private Object initialValue;

    public EntityProperty(String name, Range range, Type type, String initialValue) {
        this.name = name;
        this.range = range;
        this.type = type;
        initialValue(initialValue);
    }

    private void initialValue(String initialValue) {
        if (initialValue != null) {
            switch (type) {
                case FLOAT:
                    this.initialValue = Float.parseFloat(initialValue);
                    break;
                case BOOLEAN:
                    this.initialValue = Boolean.parseBoolean(initialValue);
                    break;
                case STRING:
                    this.initialValue = initialValue;
                    break;
                default:
                    this.initialValue = null;
            }
        } else {
            this.initialValue = null;
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

    public Object getInitialValue() {
        return initialValue;
    }

    public Object getRandomInitValue() {
        switch (type) {
            case DECIMAL:
                return range.getRandomValue(Type.DECIMAL);
            case FLOAT:
                return range.getRandomValue(Type.FLOAT);
            case BOOLEAN:
                return getRandomBooleanValue();
            case STRING:
                return getRandomStringValue();
            default:
                return null;
        }
    }

    private boolean getRandomBooleanValue() {
        return Math.random() < 0.5;
    }

    private String getRandomStringValue() {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?,_-(). ";
        Range stringLengthRange = new Range(1.0, 50.0);
        int stringLength = stringLengthRange.getRandomIntValue();
        char[] res = new char[stringLength];
        for (int i = 0; i < stringLength; i++) {
            res[i] = (characters.charAt(new Random().nextInt(characters.length())));
        }
        return Arrays.toString(res);
    }

    public Map<Object, Integer> getHistogram(List<EntityInstance> entityInstances) {
        Map<Object, Integer> histogram= new HashMap<>();
        if(this.type == Type.BOOLEAN) {
            histogram.put(true, 0);
            histogram.put(false, 0);
        }
        for (EntityInstance entityInstance : entityInstances) {
            if(!entityInstance.isAlive()) continue;
            Object value = entityInstance.getPropertyVal(name);
            if (histogram.containsKey(value)) {
                histogram.put(value, histogram.get(value) + 1);
            } else {
                histogram.put(value, 1);
            }
        }
        return histogram;
    }

    public String getInfo() {
        return "Property name: " + name + "\n" +
                " • Type:" + type.toString() + "\n" +
                " • Range: " + range.toString() + "\n" +
                " • Initial value: " + initialValue.toString() + "\n"
                ;
    }
}
