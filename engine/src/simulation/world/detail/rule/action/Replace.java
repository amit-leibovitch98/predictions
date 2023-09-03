package simulation.world.detail.rule.action;

import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;

public class Replace extends Action {
    private Entity entityToKill;
    private Entity entityToCreate;
    private boolean isFromScratch;
    private World world;

    public Replace(Entity entityToKill, Entity entityToCreate, boolean isFromScratch) {
        super(entityToKill, null);
        this.entityToKill = entityToKill;
        this.entityToCreate = entityToCreate;
        this.isFromScratch = isFromScratch;
    }
    @Override
    public void doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance) {
        throw new UnsupportedOperationException("Set action doesn't support doAction with two entity instances");
    }


    public void doAction(EntityInstance entityInstance) {
        if(entityInstance.getName().equals(this.entityToKill.getName())) {
            entityInstance.kill();
            EntityInstance replacedEntity = new EntityInstance(entityToCreate, entityInstance.getTick(), this.world.getGrid());
            if (isFromScratch) {
                world.getEntityInstancesByName(replacedEntity.getName()).add(replacedEntity);
            } else {
                for (EntityProperty property : entityInstance.getProperties()) {
                    replacedEntity.setPropertyVal(
                            property.getName(), entityInstance.getPropertyVal(property.getName())
                            //if property doesn't exist, nothing happens
                    );
                }
            }
        } else {
            throw new IllegalStateException("wrong entity to kill");
        }
    }
}
