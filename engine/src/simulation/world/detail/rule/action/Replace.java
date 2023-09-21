package simulation.world.detail.rule.action;

import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;

public class Replace extends Action {
    private Entity entityToKill;
    private Entity entityToCreate;
    private boolean isFromScratch;

    public Replace(Entity entityToKill, Entity entityToCreate, boolean isFromScratch) {
        super(entityToKill, null);
        this.entityToKill = entityToKill;
        this.entityToCreate = entityToCreate;
        this.isFromScratch = isFromScratch;
    }

    @Override
    public boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance) {
        if (sourceEntityInstance.getName().equals(this.entityToKill.getName())) {
            replace(sourceEntityInstance);
        } else if (targetEntityInstance.getName().equals(this.entityToKill.getName())) {
            replace(targetEntityInstance);
        } else {
            throw new IllegalArgumentException("sourceEntityInstance or targetEntityInstance is not of the right type");
        }
        return true;
    }

    public boolean doAction(EntityInstance entityInstance) {
        try {
            replace(entityInstance);
        } catch (IllegalStateException e) {
            System.out.println("wrong entity to kill: " + e.getMessage());
        }
        return true;
    }

    private void replace(EntityInstance entityInstance) {
        entityInstance.kill(world.getGrid());
        EntityInstance replacedEntity = new EntityInstance(entityToCreate, entityInstance.getTick(), this.world.getGrid());
        world.getEntityInstancesByName(replacedEntity.getName()).add(replacedEntity);
        if (!isFromScratch) {
            for (EntityProperty property : entityInstance.getProperties()) {
                try {
                    replacedEntity.setPropertyVal(
                            property.getName(), entityInstance.getPropertyVal(property.getName()), true
                            //if property doesn't exist, nothing happens
                    );
                } catch (IllegalArgumentException e) {
                    continue;
                }

            }
        }
    }
}
