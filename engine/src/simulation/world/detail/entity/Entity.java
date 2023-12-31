package simulation.world.detail.entity;


import simulation.world.detail.ISimulationComponent;

import java.util.List;

public class Entity implements ISimulationComponent {
    private final String name;
    private int population;
    private final List<EntityProperty> properties;

    public Entity(String name, List<EntityProperty> properties) {
        this.name = name;
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

    public String getInfo() {
        return "Entity name: " + name + "\n" +
                "Entity properties: \n" + getPropertiesInfo();

    }

    private String getPropertiesInfo() {
        StringBuilder propertiesInfo = new StringBuilder();
        for (EntityProperty property : properties) {
            propertiesInfo.append("• Property name: ").append(property.getName()).append("\n")
                    .append("   Property type: ").append(property.getType().toString()).append("\n");
            if (property.getRange() != null) {
                propertiesInfo.append("   Property range: ").append(property.getRange().toString()).append("\n");
            } else {
                propertiesInfo.append("   No range.").append("\n");
            }
            if (property.getInitialValue() != null) {
                propertiesInfo.append("   Property initial value: ").append(property.getInitialValue()).append("\n");
            } else {
                propertiesInfo.append("   No initial value.").append("\n");
            }
        }
        return propertiesInfo.toString();
    }

    public void setPopulation(Integer population) {
        if (population != null) {
            this.population = population;
        }
    }
}
