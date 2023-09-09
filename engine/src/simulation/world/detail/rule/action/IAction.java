package simulation.world.detail.rule.action;

import simulation.utils.Grid;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

public interface IAction {
    boolean doAction(EntityInstance entityInstance);
    boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance);

    //boolean checkEntity(EntityInstance entityInstance);
}
