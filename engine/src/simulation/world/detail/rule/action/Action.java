package simulation.world.detail.rule.action;

import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.action.condition.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Action implements IAction {
    protected final int ALL = -1;
    protected Entity primeryEntity;
    protected Condition slectionCond;
    protected Entity secenderyEntity;
    protected EntityProperty property = null;
    protected String propertyName;
    protected World world = null;
    protected Integer selectionCount;
    protected Condition selectionCond;

    public Action(Entity primeryEntity, String propertyName) {
        this.primeryEntity = primeryEntity;
        this.secenderyEntity = null;
        this.selectionCount = null;
        this.slectionCond = null;
        this.propertyName = propertyName;
        if (primeryEntity != null && propertyName != null) {
            this.property = primeryEntity.getProperty(propertyName);
        }
    }

    public Action(Entity primeryEntity, Entity secenderyEntity, int selectionCount, Condition selectionCond, String propertyName) {
        this.primeryEntity = primeryEntity;
        this.secenderyEntity = secenderyEntity;
        this.selectionCount = selectionCount;
        this.slectionCond = selectionCond;
        this.propertyName = propertyName;
        if (primeryEntity != null && propertyName != null) {
            this.property = primeryEntity.getProperty(propertyName);
        }
    }

    public Entity getPrimeryEntity() {
        return primeryEntity;
    }

    public List<EntityInstance> getSecenderyEntities() {
        if (secenderyEntity == null) return null;
        List<EntityInstance> entityInstances = new ArrayList<>();
        EntityInstance entityInstance = null;
        if (secenderyEntity.getName().equals(world.getPrimeryEntityInstances().get(0).getName())) {
            if (selectionCount > world.countAliveOfEntity(world.getEntities().get(0))) {
                selectionCount = world.countAliveOfEntity(world.getEntities().get(0));
            }
            for (int i = 0; i <= selectionCount; i++) {
                Random random = new Random();
                int randomNumber = random.nextInt(selectionCount);
                entityInstance = world.getPrimeryEntityInstances().get(randomNumber);
                if (entityInstance.isAlive() && slectionCond.evaluateCond(entityInstance)) {
                    entityInstances.add(entityInstance);
                }
                if (entityInstances.size() == selectionCount) break;
            }
        } else if (secenderyEntity.getName().equals(world.getSeconderyEntityInstances().get(0).getName())) {
            if (selectionCount > world.countAliveOfEntity(world.getEntities().get(1))) {
                selectionCount = world.countAliveOfEntity(world.getEntities().get(1));
            }
            for (int i = 0; i <= selectionCount; i++) {
                Random random = new Random();
                int randomNumber = random.nextInt(selectionCount);
                entityInstance = world.getSeconderyEntityInstances().get(randomNumber);
                if (entityInstance.isAlive() && slectionCond.evaluateCond(entityInstance)) {
                    entityInstances.add(entityInstance);
                }
                if (entityInstances.size() == selectionCount) break;
            }
        }
        return entityInstances;
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
