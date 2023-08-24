package simulation.world.detail.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstance extends Entity {
    boolean isAlive = true;
    //private List<EntityPropertyWraper> propertiesWithVals;
    private Map<EntityProperty, Object> propertiesWithVals;

    public EntityInstance(Entity entity) {
        super(entity.getName(), entity.getPopulation(), entity.getProperties());
        initInstanceValues(entity.getProperties());
    }

    private void initInstanceValues(List<EntityProperty> properties) {
        this.propertiesWithVals = new HashMap<>();
        for(EntityProperty property : properties) {
            Object initialValue = property.getInitialValue();
            if(initialValue == null) {
                initialValue = property.getRandomInitValue();
            }
            this.propertiesWithVals.put(property, initialValue);
        }
    }

    public Object getPropertyVal(String propertyName) {
        for(EntityProperty property : propertiesWithVals.keySet()) {
            if(property.getName().equals(propertyName)) {
                return propertiesWithVals.get(property);
            }
        }
        return null;
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

    public void setPropertyVal(String propertyName, Object value) {
        for(EntityProperty property : propertiesWithVals.keySet()) {
            if(property.getName().equals(propertyName)) {
                propertiesWithVals.put(property, value);
            }
        }
    }
    public boolean isAlive() {
        return isAlive;
    }
    public void kill() {
        isAlive = false;
    }
}
