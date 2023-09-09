package simulation.world.detail.rule.action;

import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;

public abstract class Action implements IAction {
    protected Entity entity;
    protected EntityProperty property = null;
    protected String propertyName;
    protected  World world = null;

    public Action(Entity entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        if ( entity != null && propertyName != null) {
            this.property = entity.getProperty(propertyName);
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public abstract boolean doAction(EntityInstance entityInstance);
    public abstract boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance);


    public void setWorld(World world) {
        this.world = world;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
