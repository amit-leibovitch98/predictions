package simulation.world.detail.rule.action.condition;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.rule.action.Action;

import java.util.List;

public abstract class Condition extends Action implements ICond {

    List<Action> thenAction;
    List<Action> elseAction;

    public Condition(Entity entity, String propertyName) {
        super(entity, propertyName);}
    public void setThenActions(List<Action> thenAction) {
        this.thenAction = thenAction;
    }
    public void setElseActions(List<Action> elseAction) {
        this.elseAction = elseAction;
    }
    public abstract boolean evaluateCond();
    public void activateThen() {
        if(thenAction != null) {
            for (Action thenAction : thenAction) {
                thenAction.doAction();
            }
        } else {
            throw new IllegalArgumentException("thenAction is null");
        }
    }
    public void activateElse() {
        if(elseAction != null) {
            for (Action thenAction : elseAction) {
                thenAction.doAction();
            }
        } else {
            throw new IllegalArgumentException("thenAction is null");
        }
    }


    @Override
    public void doAction() {
        if(evaluateCond()){
            activateThen();
        } else {
            activateElse();
        }

    }
}
