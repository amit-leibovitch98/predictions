package simulation.world.detail.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import simulation.utils.Grid;
import simulation.utils.Location;
import simulation.utils.expression.CondExpression;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstance extends Entity {
    private IntegerProperty tick;
    boolean isAlive = true;
    private Map<EntityProperty, Object> propertiesWithVals;
    private Map<EntityProperty, Integer> propertiesWithTicks;
    private Location location;
    private int consistencySum;
    private int consistencyCount;

    public EntityInstance(Entity entity, IntegerProperty tick, Grid grid) {
        super(entity.getName(), entity.getProperties());
        initInstanceValues(entity.getProperties(), grid);
        this.tick.bind(tick);
    }

    private void initInstanceValues(List<EntityProperty> properties, Grid grid) {
        this.tick = new SimpleIntegerProperty();
        this.propertiesWithVals = new HashMap<>();
        this.propertiesWithTicks = new HashMap<>();
        for (EntityProperty property : properties) {
            Object initialValue = property.getInitialValue();
            if (initialValue == null) {
                initialValue = property.getRandomInitValue();
            }
            this.propertiesWithVals.put(property, initialValue);
            this.propertiesWithTicks.put(property, 0);
        }
        this.location = grid.getEmptyRandLoc();
        grid.putEntityInLoc(this, location);
    }

    public IntegerProperty getTick() {
        return this.tick;
    }

    public Object getPropertyVal(String propertyName) {
        for (EntityProperty property : propertiesWithVals.keySet()) {
            if (property.getName().equals(propertyName)) {
                return propertiesWithVals.get(property);
            }
        }
        return null;
    }

    @Override
    public EntityProperty getProperty(String propertyName) {
        for (EntityProperty property : propertiesWithVals.keySet()) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getPropertyLastUpdatedTick(String propertyName) {
        for (EntityProperty property : propertiesWithVals.keySet()) {
            if (property.getName().equals(propertyName)) {
                return propertiesWithTicks.get(property);
            }
        }
        return null;
    }

    public void setPropertyVal(String propertyName, Object value) {
        if (this.getProperty(propertyName) == null) {
            throw new IllegalArgumentException("Property " + propertyName + " does not exist");
        }
        if (value instanceof CondExpression) {
            value = ((CondExpression) value).resolveExpression(this);
        }
        try {
            switch (getProperty(propertyName).getType()) {
                case DECIMAL:
                    value = Integer.parseInt(value.toString());
                    break;
                case FLOAT:
                    value = Float.parseFloat(value.toString());
                    break;
                case BOOLEAN:
                    if(!(value instanceof Boolean)) {
                        if (value.equals("false")) {
                            value = false;
                        } else if (value.equals("true")) {
                            value = true;
                        } else {
                            throw new IllegalArgumentException("Invalid value for boolean type: " + value);
                        }
                    }
                    break;
                case STRING:
                    value = value.toString();
                    break;
            }
            propertiesWithTicks.put(getProperty(propertyName), tick.get());
            consistencyCount++;
            consistencySum += tick.get();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for " + getProperty(propertyName).getType() + " type: " + value);
        }
        propertiesWithVals.put(getProperty(propertyName), value);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill(Grid grid) {
        isAlive = false;
        grid.setLocAsEmpty(location);
    }

    public void move(Grid grid) {
        this.location = grid.move(this);
    }

    public float getConsistency() {
        return (float) consistencySum / consistencyCount;
    }
}
