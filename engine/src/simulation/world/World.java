package simulation.world;

import simulation.Simulation;
import simulation.utils.Grid;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.action.Action;


import java.util.ArrayList;
import java.util.List;

public class World extends WorldDef {
    private List<EnvironmentVariable> environmentVars;
    private List<Entity> entities;
    private List<EntityInstance> primeryEntityInstances;
    private List<EntityInstance> seconderyEntityInstances;
    private Grid grid;

    protected World(List<EnvironmentVariable> environmentVars, List<Entity> entities, List<Rule> rules,
                    TerminationCond terminationCond, Grid grid, List<Action> actions) {
        super(environmentVars, entities, grid, actions);

        this.environmentVars = new ArrayList<>();
        this.terminationConds = terminationCond;
        this.rules = rules;
        this.grid = new Grid(grid);

        for(EnvironmentVariable envVar : environmentVars) {
            this.environmentVars.add(new EnvironmentVariable(envVar.getName(), envVar.getRange(), envVar.getType()));
        }

        this.entities = new ArrayList<>();
        for(Entity entity : entities) {
            this.entities.add(new Entity(entity.getName(), entity.getProperties()));
        }

        for (Action action : actions) {
            action.setWorld(this);
        }
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
    public void initPopulation(Simulation simulation) {
        this.primeryEntityInstances = new ArrayList<>();
        for (int i = 0; i < entities.get(0).getPopulation(); i++) {
            this.primeryEntityInstances.add(new EntityInstance(entities.get(0), simulation.getTick(), grid));
        }
        this.seconderyEntityInstances = new ArrayList<EntityInstance>();
        for (int i = 0; i < entities.get(1).getPopulation(); i++) {
            this.seconderyEntityInstances.add(new EntityInstance(entities.get(1), simulation.getTick(), grid));
        }
    }

    public List<EntityInstance> getPrimeryEntityInstances() {
        return primeryEntityInstances;
    }
    public List<EntityInstance> getSeconderyEntityInstances() {
        return seconderyEntityInstances;
    }


    public String countAliveOfEntity(Entity entity) {
        int alive = 0;
        for (EntityInstance entityInstance : primeryEntityInstances) {
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

    public List<EntityInstance> getEntityInstancesByName(String entityName) {
        if(entities.get(0).getName().equals(entityName)) {
            return primeryEntityInstances;
        } else if(entities.get(1).getName().equals(entityName)) {
            return seconderyEntityInstances;
        } else {
            throw new RuntimeException("Entity " + entityName + " not found");
        }
    }
}
