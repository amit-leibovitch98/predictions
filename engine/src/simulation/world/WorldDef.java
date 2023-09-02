package simulation.world;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;


import java.util.ArrayList;
import java.util.List;

public class WorldDef {
    protected List<Rule> rules;
    protected TerminationCond terminationConds;
    private final List<Entity> entities;
    private final List<EnvironmentVariable> environmentVars;

    public WorldDef(List<EnvironmentVariable> environmentVariableList, List<Entity> entityList) {
        this.environmentVars = environmentVariableList;
        this.entities = entityList;
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
    public World createWorld() {
        return new World(this.environmentVars, this.entities, this.rules, this.terminationConds);
    }
}
