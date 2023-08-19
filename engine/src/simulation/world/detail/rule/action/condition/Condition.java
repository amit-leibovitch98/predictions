package simulation.world.detail.rule.action.condition;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
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
    public abstract boolean evaluateCond(EntityInstance entityInstance);
    public void activateThen(EntityInstance entityInstance) {
        if(thenAction != null) {
            for (Action thenAction : thenAction) {
                thenAction.doAction(entityInstance);
            }
        } else {
            throw new IllegalArgumentException("thenAction is null");
        }
    }
    public void activateElse(EntityInstance entityInstance) {
        if(elseAction != null) {
            for (Action thenAction : elseAction) {
                thenAction.doAction(entityInstance);
            }
        } else {
            throw new IllegalArgumentException("thenAction is null");
        }
    }


    @Override
    public void doAction(EntityInstance entityInstance) {
        if(evaluateCond(entityInstance)){
            activateThen(entityInstance);
        } else {
            activateElse(entityInstance);
        }

    }
}
