package simulation.world.detail.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import simulation.Simulation;
import simulation.utils.Grid;
import simulation.utils.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstance extends Entity {
    private IntegerProperty tick;
    boolean isAlive = true;
    private Map<EntityProperty, Object> propertiesWithVals;
    private Map<EntityProperty, Integer> propertiesWithTicks;
    private Location location;

    public EntityInstance(Entity entity, IntegerProperty tick, Grid grid) {
        super(entity.getName(), entity.getProperties());
        initInstanceValues(entity.getProperties(), grid);
        tick.bind(tick);
    }

    private void initInstanceValues(List<EntityProperty> properties, Grid grid) {
        this.tick = new SimpleIntegerProperty();
        this.propertiesWithVals = new HashMap<>();
        this.propertiesWithTicks = new HashMap<>();
        for(EntityProperty property : properties) {
            Object initialValue = property.getInitialValue();
            if(initialValue == null) {
                initialValue = property.getRandomInitValue();
            }
            this.propertiesWithVals.put(property, initialValue);
            this.propertiesWithTicks.put(property, 0);
        }
        this.location = grid.getEmptyRandLoc();
        grid.putEntityInLoc(this, location);
    }

    public Object getPropertyVal(String propertyName) {
        for(EntityProperty property : propertiesWithVals.keySet()) {
            if(property.getName().equals(propertyName)) {
                return propertiesWithVals.get(property);
            }
        }
        return null;
    }

    public IntegerProperty getTick() {
        return this.tick;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public EntityProperty getProperty(String propertyName) {
        for(EntityProperty property : propertiesWithVals.keySet()) {
            if(property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    public Integer getPropertyLastUpdatedTick(String propertyName) {
        for(EntityProperty property : propertiesWithVals.keySet()) {
            if(property.getName().equals(propertyName)) {
                return propertiesWithTicks.get(property);
            }
        }
        return null;
    }

    public void setPropertyVal(String propertyName, Object value) {
        for(EntityProperty property : propertiesWithVals.keySet()) {
            if(property.getName().equals(propertyName)) {
                propertiesWithVals.put(property, value);
                propertiesWithTicks.put(property, tick.getValue());
            }
        }
    }
    public boolean isAlive() {
        return isAlive;
    }
    public void kill() {
        isAlive = false;
    }
    public void move(Grid grid) {
        this.location = grid.move(this);
    }

}
