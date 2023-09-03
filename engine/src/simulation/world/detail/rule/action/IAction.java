package simulation.world.detail.rule.action;

import simulation.utils.Grid;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

public interface IAction {
    void doAction(EntityInstance entityInstance);
    void doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance);
}
