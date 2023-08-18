package simulation.world.detail.rule;

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
}
