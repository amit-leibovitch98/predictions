package simulation.world.detail.rule;

import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;

import java.util.ArrayList;
import java.util.List;

public class Rule implements ISimulationComponent {
    private final Activation activation;
    private final String name;
    private List<Action> actions;
    int ticksCount;

    public Rule(String name, Activation activation, List<Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actions = actions;
        this.ticksCount = 0;
    }

    public Activation getActivation() {
        return activation;
    }

    public List<Action> getActions() {
        return actions;
    }

    public String getName() {
        return name;
    }

    public void activateRule(EntityInstance entityInstance, int ticksCount) {
        if((ticksCount + 1) % activation.getTicks() == 0) {
            float activationOdd = (float) Math.random();
            if (activationOdd <= activation.getProbability()) {
                for (Action action : actions) {
                    action.doAction(entityInstance);
                }
            }
        }
    }
    public String getInfo() {
        return "Rule name: " + name + "\n" +
                "Rule activation: " + activation.toString() + "\n" +
                "Rule number of actions: " + actions.size();
    }
}
