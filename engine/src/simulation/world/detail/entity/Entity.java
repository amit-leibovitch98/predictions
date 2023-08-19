package simulation.world.detail.entity;


import java.util.List;

public class Entity {
    private final String name;
    private final int population;
    private final List<EntityProperty> properties;

    public Entity(String name, int population, List<EntityProperty> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }
    public String getName() {
        return name;
    }
    public int getPopulation() {
        return population;
    }
    public EntityProperty getProperty(String name) {
        for (EntityProperty property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public List<EntityProperty> getProperties() {
        return properties;
    }
}
