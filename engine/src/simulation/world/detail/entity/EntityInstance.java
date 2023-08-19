package simulation.world.detail.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityInstance extends Entity {
    boolean isAlive = true;
    private List<EntityPropertyWraper> propertiesWithVals;
    public EntityInstance(Entity entity, List<EntityProperty> properties) {
        super(entity.getName(), entity.getPopulation(), entity.getProperties());
        initInstanceValues(properties);
    }

    private void initInstanceValues(List<EntityProperty> properties) {
        this.propertiesWithVals = new ArrayList<EntityPropertyWraper>();
        for(EntityProperty property : properties) {
            Object initialValue = property.getInitialValue();
            if(initialValue == null) {
                initialValue = property.getRange().getRandomValue();
            }
            this.propertiesWithVals.add(new EntityPropertyWraper(property, initialValue));
        }
    }

    public Object getPropertyVal(String propertyName) {
        for(EntityPropertyWraper property : propertiesWithVals) {
            if(property.getProperty().getName().equals(propertyName)) {
                return property.getValue();
            }
        }
        return null;
    }

    @Override
    public EntityProperty getProperty(String propertyName) {
        for(EntityPropertyWraper property : propertiesWithVals) {
            if(property.getProperty().getName().equals(propertyName)) {
                return property.getProperty();
            }
        }
        return null;
    }

    public void setPropertyVal(String propertyName, Object value) {
        for(EntityPropertyWraper property : propertiesWithVals) {
            if(property.getProperty().getName().equals(propertyName)) {
                property.setValue(value);
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
