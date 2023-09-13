package simulation.world.detail.rule.action;

import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.condition.Condition;

import java.beans.Expression;

public class Set extends Action {
    private final CondExpression value;

    public Set(Entity primeryEntity, String propertyName, CondExpression value) {
        super(primeryEntity, propertyName);
        this.value = value;
    }

    public Set(Entity primeryEntity, Entity secondaryEntity, int selectionCount, Condition selectionCond, String propertyName, CondExpression value) {
        super(primeryEntity, secondaryEntity, selectionCount, selectionCond, propertyName);
        this.value = value;
    }

    @Override
    public boolean doAction(EntityInstance entityInstance) {
        entityInstance.setPropertyVal(propertyName, value, false);
        return true;
    }
    @Override
    public boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance) {
        throw new UnsupportedOperationException("Set action doesn't support doAction with two entity instances");
    }
}
