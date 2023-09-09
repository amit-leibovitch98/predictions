package simulation.world.detail.rule.action;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

import java.util.List;

public class Kill extends Action{
    public Kill(Entity entity) {
        super(entity, "");
    }

    @Override
    public boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance) {
        throw new UnsupportedOperationException("Set action doesn't support doAction with two entity instances");
    }
    @Override
    public boolean doAction(EntityInstance entityInstance) {
        kill(entityInstance);
        return true;
    }

    private void kill(EntityInstance entityInstance) {
        entityInstance.kill(world.getGrid());
    }
}
