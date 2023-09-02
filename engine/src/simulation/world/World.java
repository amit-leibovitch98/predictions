package simulation.world;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.environmentvariables.EnvironmentVariable;


import java.util.ArrayList;
import java.util.List;

public class World extends WorldDef {
    private List<EnvironmentVariable> environmentVars;
    private List<Entity> entities;
    private List<EntityInstance> entityInstances;

    protected World(List<EnvironmentVariable> environmentVars, List<Entity> entities, List<Rule> rules, TerminationCond terminationCond) {
        super(environmentVars, entities);
        this.environmentVars = new ArrayList<>();
        for(EnvironmentVariable envVar : environmentVars) {
            this.environmentVars.add(new EnvironmentVariable(envVar.getName(), envVar.getRange(), envVar.getType()));
        }
        this.entities = new ArrayList<>();
        for(Entity entity : entities) {
            this.entities.add(new Entity(entity.getName(), entity.getPopulation(), entity.getProperties()));
        }
        this.terminationConds = terminationCond;
        this.rules = rules;
    }

    public List<EnvironmentVariable> getEnvironmentVars() {
        return environmentVars;
    }
    public List<Entity> getEntities() {
        return entities;
    }

    public void setEnvironmentVars(List<EnvironmentVariable> environmentVars) {
        this.environmentVars = environmentVars;
    }
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
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
