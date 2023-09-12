package simulation.world.detail.rule.action;

import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

import java.beans.Expression;

public class Set extends Action {
    private final CondExpression value;

    public Set(Entity entity, String propertyName, CondExpression value) {
        super(entity, propertyName);
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
