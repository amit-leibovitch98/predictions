package simulation.world.detail.rule.action;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.action.condition.IAction;

public abstract class Action implements IAction {
    protected Entity entity = null;
    protected EntityProperty property = null;
    protected String propertyName = null;

    public Action(Entity entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.property = entity.getProperty(propertyName);
    }

    public abstract void doAction(EntityInstance entityInstance);
}
