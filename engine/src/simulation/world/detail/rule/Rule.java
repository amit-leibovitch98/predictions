package simulation.world.detail.rule;

import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private final Activation activation;
    private final String name;
    private List<Action> actions;

    public Rule(String name, Activation activation, List<Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actions = actions;
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

    public void activateRule(EntityInstance entityInstance) {
        float activationOdd = (float) Math.random();
        if(activationOdd <= activation.getProbability()) {
            for(Action action : actions) {
                action.doAction(entityInstance);
            }
        }
    }
}
