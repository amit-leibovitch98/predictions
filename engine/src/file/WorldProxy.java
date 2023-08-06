package file;

import file.schema.generated.*;
import simulation.utils.Range;
import simulation.utils.Type;
import simulation.utils.Value;
import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.entity.EntityProperty;

import java.util.ArrayList;
import java.util.List;

public class WorldProxy {
    PRDWorld prdWorld;

    public WorldProxy(PRDWorld prdWorld) {
        this.prdWorld = prdWorld;
    }

    public World getWorld() {
        World world = new World();
        //TODO: implement using funcs from here
        world.setEnvironmentVars(getEnvironmentVariableList());
        return world;
    }

    private List<EnvironmentVariable> getEnvironmentVariableList() {
        List<EnvironmentVariable> environmentVariables = new ArrayList<EnvironmentVariable>();
        PRDEvironment prdEnvironments = prdWorld.getPRDEvironment();

        for (PRDEnvProperty prdEnvProperty : prdEnvironments.getPRDEnvProperty()) {
            PRDRange prdRange = prdEnvProperty.getPRDRange();
            environmentVariables.add(new EnvironmentVariable(
                    prdEnvProperty.getPRDName(),
                    new Range(prdRange.getFrom(), prdRange.getTo()),
                    Type.fromString(prdEnvProperty.getType())
            ));
        }
        return environmentVariables;
    }

    private List<Entity> getEntityList() {
        List<Entity> entities = new ArrayList<Entity>();
        PRDEntities prdEntities = prdWorld.getPRDEntities();
        for (PRDEntity prdEntity : prdEntities.getPRDEntity()) {
            entities.add(new Entity(
                    prdEntity.getName(),
                    prdEntity.getPRDPopulation(),
                    getEntittProperties(prdEntity.getPRDProperties())
            ));

        }

        return entities;
    }

    private List<Rule> getRuleList() {
        List<Rule> rules = new ArrayList<Rule>();
        //TODO: implement
        return rules;
    }

    private List<TerminationCond> getTerminationList() {
        List<TerminationCond> terminationConds = new ArrayList<TerminationCond>();
        //TODO: implement
        return terminationConds;
    }

    private List<EntityProperty> getEntittProperties(PRDProperties prdProperties) {
        List<EntityProperty> properties = new ArrayList<EntityProperty>();
        for (PRDProperty prdProperty : prdProperties.getPRDProperty()) {
            PRDRange prdRange = prdProperty.getPRDRange();
            PRDValue prdValue = prdProperty.getPRDValue();

            properties.add(new EntityProperty(
                    prdProperty.getPRDName(),
                    new Range(prdRange.getFrom(), prdRange.getTo()),
                    new Value(prdValue.getInit(), prdValue.isRandomInitialize()),
                    Type.fromString(prdProperty.getType())
            ));
        }
        return properties;
    }

}
