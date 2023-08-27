package simulation.world.detail.entity;

import simulation.utils.Range;
import simulation.utils.Type;
import simulation.utils.Value;

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
            case FLOAT:
                return range.getRandomValue();
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
        Map<Object, Integer> hostogram= new HashMap<>();
        for (EntityInstance entityInstance : entityInstances) {
            Object value = entityInstance.getPropertyVal(name);
            if (hostogram.containsKey(value)) {
                hostogram.put(value, hostogram.get(value) + 1);
            } else {
                hostogram.put(value, 1);
            }
        }
        return hostogram;
    }

    public String getInfo() {
        return "Property name: " + name + "\n" +
                " • Type:" + type.toString() + "\n" +
                " • Range: " + range.toString() + "\n" +
                " • Initial value: " + initialValue.toString() + "\n"
                ;
    }
}
