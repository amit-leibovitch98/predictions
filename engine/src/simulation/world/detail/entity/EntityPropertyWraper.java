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
        switch (property.getType()) {
            case FLOAT:
                this.value = Float.parseFloat(value.toString());
                break;
            case DECIMAL:
                this.value = Integer.parseInt(value.toString());
                break;
            case STRING:
                this.value = value.toString();
                break;
            default:
                throw new RuntimeException("Unknown property type");
        }
    }
}
