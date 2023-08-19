package simulation.world;

import simulation.world.detail.entity.Entity;
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

    private List<List<String>> getEntitiesDetails() {
        List<List<String>> entitiesDetails = new ArrayList<List<String>>();
        entitiesDetails.add(new ArrayList<String>());
        entitiesDetails.get(0).add("Entities:");
        for (int i = 1; i < entities.size() + 1; i++) {
            entitiesDetails.add(new ArrayList<String>());
            entitiesDetails.get(i).add(" " + i + ". " + entities.get(i - 1).getName());
            entitiesDetails.get(i).add("    population:" + Integer.toString(+ entities.get(i - 1).getPopulation()));
            for(EntityProperty property : entities.get(i - 1).getProperties()) {
                entitiesDetails.get(i).add("    •property name: " + property.getName());
                entitiesDetails.get(i).add("      -property type: " + property.getType().toString());
                entitiesDetails.get(i).add("      -property range: " + property.getRange().toString());
                entitiesDetails.get(i).add("      -is property randomly initialized: " + Boolean.toString(property.getInitialValue() == null));
            }
        }
        return entitiesDetails;
    }

    private List<List<String>> getRulesDetails(){
        List<List<String>> rulesDetails = new ArrayList<List<String>>();
        rulesDetails.add(new ArrayList<String>());
        rulesDetails.get(0).add("Rules:");
        for (int i = 1; i < rules.size() + 1; i++) {
            rulesDetails.add(new ArrayList<String>());
            rulesDetails.get(i).add(" " + i + ". " + rules.get(i - 1).getName());
            rulesDetails.get(i).add("    " + rules.get(i - 1).getActivation().toString());
            rulesDetails.get(i).add("    Number of actions: " + rules.get(i - 1).getActions().size());
        }
        return rulesDetails;
    }

    private List<List<String>> getTerminationCondsDetails() {
        List<List<String>> terminationConds = new ArrayList<List<String>>();
        terminationConds.add(new ArrayList<String>());
        terminationConds.get(0).add("Termination Conditions:");
        terminationConds.get(0).add(" •Ticks: " + Integer.toString(this.terminationConds.getByTicks()));
        terminationConds.get(0).add(" •Time: " + Integer.toString(this.terminationConds.getByTime()));
        return terminationConds;
    }

    public List<List<List<String>>> getWorldDeatils(){
        List<List<List<String>>> worldDetails = new ArrayList<List<List<String>>>();
        worldDetails.add(getEntitiesDetails());
        worldDetails.add(getRulesDetails());
        worldDetails.add(getTerminationCondsDetails());
        return worldDetails;
    }
}
