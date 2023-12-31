package simulation.world.detail.rule.action.condition;

import simulation.world.detail.entity.EntityInstance;

public interface ICond {
    boolean evaluateCond(EntityInstance entityInstance, EntityInstance secondaryEntityInstance);
    void activateThen(EntityInstance entityInstance, EntityInstance seceondaryEntityInstance);
    void activateElse(EntityInstance entityInstance, EntityInstance seceondaryEntityInstance);
    boolean evaluateCond(EntityInstance entityInstance);
    void activateThen(EntityInstance entityInstance);
    void activateElse(EntityInstance entityInstance);
}
