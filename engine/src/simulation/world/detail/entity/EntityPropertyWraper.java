package simulation.world.detail.entity;

public class EntityPropertyWraper {
    private EntityProperty property;
    private Object value;
    public EntityPropertyWraper(EntityProperty property, Object value) {
        this.property = property;
        this.value = value;
    }

    public EntityProperty getProperty() {
        return property;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
}
