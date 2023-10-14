package simulation.world.detail.rule.action.condition;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;

import java.util.List;

public abstract class Condition extends Action implements ICond {

    List<Action> thenAction;
    List<Action> elseAction;

    public Condition(Entity primeryentity, String propertyName) {
        super(primeryentity, propertyName);
    }

    public Condition(Entity primeryentity, Entity secondaryentity, int selectionCount, Condition selectionCond, String propertyName) {
        super(primeryentity, secondaryentity, selectionCount, selectionCond, propertyName);
    }

    public void setThenActions(List<Action> thenAction) {
        this.thenAction = thenAction;
    }
    public void setElseActions(List<Action> elseAction) {
        this.elseAction = elseAction;
    }
    public abstract boolean evaluateCond(EntityInstance entityInstance);
    public abstract boolean evaluateCond(EntityInstance entityInstance, EntityInstance secondaryEntityInstance);

    public void activateThen(EntityInstance entityInstance, EntityInstance seceondaryEntityInstance) {
        if(thenAction != null) {
            for (Action thenAction : thenAction) {
                thenAction.doAction(entityInstance, seceondaryEntityInstance);
            }
        }
    }
    public void activateThen(EntityInstance entityInstance) {
        if(thenAction != null) {
            for (Action thenAction : thenAction) {
                thenAction.doAction(entityInstance);
            }
        }
    }
    public void activateElse(EntityInstance entityInstance, EntityInstance seceondaryEntityInstance) {
        if(elseAction != null) {
            for (Action thenAction : elseAction) {
                thenAction.doAction(entityInstance, seceondaryEntityInstance);
            }
        }
    }
    public void activateElse(EntityInstance entityInstance) {
        if(elseAction != null) {
            for (Action thenAction : elseAction) {
                thenAction.doAction(entityInstance);
            }
        }
    }

    @Override
    public boolean doAction(EntityInstance entityInstance) {
        boolean result = false;
        if(secenderyEntity != null) {
            List<EntityInstance> secederyInstances = getSecenderyEntities();
            for (EntityInstance secenderyEntityInstance : secederyInstances) {
                result = result || doAction(entityInstance, secenderyEntityInstance);
            }
            return result;
        } else {
            if (evaluateCond(entityInstance)) {
                activateThen(entityInstance);
                return true;
            } else {
                activateElse(entityInstance);
                return false;
            }
        }
    }

    @Override
    public boolean doAction(EntityInstance primeryEntityInstance, EntityInstance secendatyEntityInstance) {
        if(primeryEntityInstance.getName().equals(primeryEntity.getName())) {
            if (evaluateCond(primeryEntityInstance)) {
                activateThen(primeryEntityInstance, secendatyEntityInstance);
                return true;
            } else {
                activateElse(primeryEntityInstance, secendatyEntityInstance);
                return false;
            }
        } else if (primeryEntityInstance.getName().equals(secenderyEntity.getName())) {
            if (evaluateCond(secendatyEntityInstance)) {
                activateThen(secendatyEntityInstance, primeryEntityInstance);
                return true;
            } else {
                activateElse(secendatyEntityInstance, primeryEntityInstance);
                return false;
            }
        } else {
            throw new RuntimeException("EntityInstance is not a valid sourceEntityInstance or targetEntityInstance");
        }
    }

    public void setSecenderyEntitySelection(Entity secendaryEntity, int selectionCount, Condition selectionCond) {
        this.secenderyEntity = secendaryEntity;
        this.selectionCount = selectionCount;
        this.slectionCond = selectionCond;

    }
}
