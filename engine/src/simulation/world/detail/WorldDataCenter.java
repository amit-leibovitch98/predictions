package simulation.world.detail;

import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class WorldDataCenter {
    private World world;

    public WorldDataCenter(World world) {
        this.world = world;
    }

    private List<List<String>> getEntitiesDetails() {
        List<Entity> entities = world.getEntities();
        List<List<String>> entitiesDetails = new ArrayList<List<String>>();
        entitiesDetails.add(new ArrayList<String>());
        entitiesDetails.get(0).add("Entities:");
        for (int i = 1; i < entities.size() + 1; i++) {
            entitiesDetails.add(new ArrayList<String>());
            entitiesDetails.get(i).add(" " + i + ". " + entities.get(i - 1).getName());
            entitiesDetails.get(i).add("    population:" + Integer.toString(+entities.get(i - 1).getPopulation()));
            for (EntityProperty property : entities.get(i - 1).getProperties()) {
                entitiesDetails.get(i).add("    •property name: " + property.getName());
                entitiesDetails.get(i).add("      -property type: " + property.getType().toString());
                if (property.getRange() != null) {
                    if (property.getRange() != null) {
                        entitiesDetails.get(i).add("      -property range: " + property.getRange().toString());
                    }
                }
                entitiesDetails.get(i).add("      -is property randomly initialized: " + Boolean.toString(property.getInitialValue() == null));
            }
        }
        return entitiesDetails;
    }

    private List<List<String>> getRulesDetails() {
        List<Rule> rules = world.getRules();
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
        TerminationCond terminationConds = world.getTerminationConds();
        List<List<String>> terminationCondsStrings = new ArrayList<List<String>>();
        terminationCondsStrings.add(new ArrayList<String>());
        terminationCondsStrings.get(0).add("Termination Conditions:");
        if (terminationConds.getByTicks() != null) {
            terminationCondsStrings.get(0).add(" •Ticks: " + Integer.toString(terminationConds.getByTicks()));
        }
        if (terminationConds.getByTime() != null) {
            terminationCondsStrings.get(0).add(" •Time: " + Integer.toString(terminationConds.getByTime()));
        }
        return terminationCondsStrings;
    }

    public List<List<List<String>>> getWorldDeatils() {
        List<List<List<String>>> worldDetails = new ArrayList<List<List<String>>>();
        worldDetails.add(getEntitiesDetails());
        worldDetails.add(getRulesDetails());
        worldDetails.add(getTerminationCondsDetails());
        return worldDetails;
    }
}
