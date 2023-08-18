package simulation.world;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.environmentvariables.EnvironmentVariable;


import java.util.List;

public class World {
    private List<EnvironmentVariable> environmentVars;
    private List<Entity> entities;
    private List<Rule> rules;
    private TerminationCond terminationConds;

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
}
