package simulation.world.detail.rule;

import simulation.world.detail.rule.action.Action;

public class Rule {
    private final Action action;
    private final Activation activation;
    private final String name;

    public Rule(String name, Action action, Activation activation) {
        this.name = name;
        this.action = action;
        this.activation = activation;
    }
}
