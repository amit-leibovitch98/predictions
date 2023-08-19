package simulation.world;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.environmentvariables.EnvironmentVariable;


import java.util.ArrayList;
import java.util.List;

public class World {
    private List<EnvironmentVariable> environmentVars;
    private List<Entity> entities;
    private List<Rule> rules;
    private TerminationCond terminationConds;
    private List<EntityInstance> entityInstances;

    public List<EnvironmentVariable> getEnvironmentVars() {
        return environmentVars;
    }
    public List<Entity> getEntities() {
        return entities;
    }
    public List<Rule> getRules() {
        return rules;
    }
    public TerminationCond getTerminationConds() { return terminationConds; }
    public void setEnvironmentVars(List<EnvironmentVariable> environmentVars) {
        this.environmentVars = environmentVars;
    }
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
    public void setTerminationConds(TerminationCond terminationConds) {
        this.terminationConds = terminationConds;
    }

    public EnvironmentVariable getEnvironmentVar(String name) {
        for (EnvironmentVariable var : environmentVars) {
            if (var.getName().equals(name)) {
                return var;
            }
        }
        return null;
    }
    public void initPopulation() { //fix for ex2
        this.entityInstances = new ArrayList<EntityInstance>();
        for (int i = 0; i < entities.get(0).getPopulation(); i++) {
            this.entityInstances.add(new EntityInstance(entities.get(0)));
        }
    }

    public List<EntityInstance> getEntityInstances() {
        return entityInstances;
    }


    public String countAliveOfEntity(Entity entity) {
        int alive = 0;
        for (EntityInstance entityInstance : entityInstances) {
            if (entityInstance.isAlive()) {
                alive++;
            }
        }
        return String.valueOf(alive);
    }

    public Entity getEntityByName(String entityName) {
        for (Entity entity : this.entities) {
            if (entity.getName().equals(entityName)) {
                return entity;
            }
        }
        throw new RuntimeException("Entity " + entityName + " not found");
    }

    public EnvironmentVariable getEnvVarsByName(String envVarName) {
        for (EnvironmentVariable envVar : this.environmentVars) {
            if (envVar.getName().equals(envVarName)) {
                return envVar;
            }
        }
        throw new RuntimeException("Environment Variable " + envVarName + " not found");
    }
}
