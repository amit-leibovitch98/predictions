package simulation.world.detail.rule;

import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;
import simulation.world.detail.rule.action.Proximity;
import simulation.world.detail.rule.action.Replace;

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

    public void activateRule(EntityInstance primeryEntityInstance, int ticksCount, List<EntityInstance> secoderyEntityInstances) {
        if ((ticksCount + 1) % activation.getTicks() == 0) {
            float activationOdd = (float) Math.random();
            if (activationOdd <= activation.getProbability()) {
                for (Action action : actions) {
                    if (action.getPrimeryEntity() == null || action.getPrimeryEntity().getName().equals(primeryEntityInstance.getName())) {
                        try {
                            if (action instanceof Replace || action instanceof Proximity) {
                                for (EntityInstance secoderyEntityInstance : secoderyEntityInstances) {
                                    if(secoderyEntityInstance.isAlive()) {
                                        if (action.doAction(primeryEntityInstance, secoderyEntityInstance)) { //action has actually been done
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if(action.getSecenderyEntities() != null) {
                                    for (EntityInstance secoderyEntityInstance : secoderyEntityInstances) {
                                        if(secoderyEntityInstance.isAlive()) {
                                            if (action.doAction(secoderyEntityInstance)) { //action has actually been done
                                                break;
                                            }
                                        }
                                    }
                                }
                                action.doAction(primeryEntityInstance);
                            }
                        } catch (Exception e) {
                            System.out.println("Rule: " + name + " failed to activate action: " + action.getType() + ": "
                                            + e.getMessage() + " --> Simulation failed.");
                            System.exit(1);
                        }
                    }
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
