package simulation.world;

import simulation.utils.Grid;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.rule.action.Action;


import java.util.ArrayList;
import java.util.List;

public class WorldDef {
    protected List<Rule> rules;
    protected TerminationCond terminationConds;
    private final List<Entity> entities;
    private final List<EnvironmentVariable> environmentVars;
    private final Grid grid;
    private final List<Action> actions;

    public WorldDef(List<EnvironmentVariable> environmentVariableList, List<Entity> entityList, Grid grid, List<Action> action) {
        this.environmentVars = environmentVariableList;
        this.entities = entityList;
        this.grid = grid;
        this.actions = action;
    }

    public List<Rule> getRules() {
        return rules;
    }
    public TerminationCond getTerminationConds() { return terminationConds; }
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
    public void setTerminationConds(TerminationCond terminationConds) {
        this.terminationConds = terminationConds;
    }
    public TerminationCond getTerminationCond() {
        return this.terminationConds;
    }
    public List<EnvironmentVariable> getEnvironmentVars() {
        return environmentVars;
    }
    public List<Entity> getEntities() {
        return entities;
    }
    public Grid getGrid() {
        return grid;
    }
    public World createWorld() {
        return new World(this.environmentVars, this.entities, this.rules, this.terminationConds, this.grid, this.actions);
    }

    public void updateActions(World world) {
        for (Action action : actions) {
            action.setWorld(world);
        }
    }
}
