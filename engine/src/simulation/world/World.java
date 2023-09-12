package simulation.world;

import simulation.Simulation;
import simulation.utils.Grid;
import simulation.utils.Type;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.action.Action;


import java.util.ArrayList;
import java.util.List;

public class World extends WorldDef implements Cloneable{
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
        this.grid = new Grid(grid.getRows(), grid.getCols());

        for (EnvironmentVariable envVar : environmentVars) {
            this.environmentVars.add(new EnvironmentVariable(envVar.getName(), envVar.getRange(), envVar.getType()));
        }

        this.entities = new ArrayList<>();
        for (Entity entity : entities) {
            this.entities.add(new Entity(entity.getName(), entity.getProperties()));
        }

        for (Action action : actions) {
            action.setWorld(this);
        }
    }

    @Override
    public Grid getGrid() {
        return grid;
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
            this.primeryEntityInstances.add(new EntityInstance(entities.get(0), simulation.getTick(), this.grid));
        }
        this.seconderyEntityInstances = new ArrayList<>();
        for (int i = 0; i < entities.get(1).getPopulation(); i++) {
            this.seconderyEntityInstances.add(new EntityInstance(entities.get(1), simulation.getTick(), this.grid));
        }
    }

    public List<EntityInstance> getPrimeryEntityInstances() {
        return primeryEntityInstances;
    }

    public List<EntityInstance> getSeconderyEntityInstances() {
        return seconderyEntityInstances;
    }

    public int countAliveOfEntity(Entity entity) {
        int alive = 0;
        if (entity.getName().equals(primeryEntityInstances.get(0).getName())) {
            List<EntityInstance> primeryEntityInstancesCpy = new ArrayList<>(primeryEntityInstances);
            for (EntityInstance entityInstance : primeryEntityInstancesCpy) {
                if (entityInstance.isAlive()) {
                    alive++;
                }
            }
        } else if (entity.getName().equals(seconderyEntityInstances.get(0).getName())) {
            List<EntityInstance> seconderyEntityInstancesCpy = new ArrayList<>(seconderyEntityInstances);
            for (EntityInstance entityInstance : seconderyEntityInstancesCpy) {
                if (entityInstance.isAlive()) {
                    alive++;
                }
            }
        } else {
            throw new RuntimeException("Entity " + entity.getName() + " not found");
        }
        return alive;
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
        if (entities.get(0).getName().equals(entityName)) {
            return primeryEntityInstances;
        } else if (entities.get(1).getName().equals(entityName)) {
            return seconderyEntityInstances;
        } else {
            throw new RuntimeException("Entity " + entityName + " not found");
        }
    }

    public float getAverageProperty(Entity entity, EntityProperty prop) {
        int count;
        float sum = 0;
        if (entity.getName().equals(primeryEntityInstances.get(0).getName())) {
            sum = getSum(prop, sum, primeryEntityInstances);
        } else if (entity.getName().equals(seconderyEntityInstances.get(0).getName())) {
            sum = getSum(prop, sum, seconderyEntityInstances);
        } else {
            throw new RuntimeException("Entity " + entity.getName() + " not found");
        }
        count = countAliveOfEntity(entity);
        return sum / count;
    }

    private float getSum(EntityProperty prop, float sum, List<EntityInstance> EntityInstances) {
        for (EntityInstance entityInstance : EntityInstances) {
            if (!entityInstance.isAlive()) continue;
            try {
                if (prop.getType() == Type.DECIMAL) {
                    sum += (int) entityInstance.getPropertyVal(prop.getName()) * 1.0f;
                } else if (prop.getType() == Type.FLOAT) {
                    sum += (float) entityInstance.getPropertyVal(prop.getName());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return sum;
    }


    @Override
    public World clone() {
       return super.createWorld();
    }
}
