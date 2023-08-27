package simulation.world.detail.rule.action;

import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

public class Decrease extends Action {
    private CondExpression by;

    public Decrease(Entity entity, String propertyName, CondExpression by) {
        super(entity, propertyName);
        this.by = by;
    }

    public void doAction(EntityInstance entityInstance) {
        decrease(entityInstance);
    }

    private void decrease(EntityInstance entityInstance) {
        Object newValue = null;
        Object byValue = by.resolveExpression();
        Object ptopValue = entityInstance.getPropertyVal(propertyName);
        if (ptopValue instanceof Float) {
            newValue = (float) ptopValue - (float) byValue;
        } else {
            throw new RuntimeException("Increase action not supported for type " + entityInstance.getProperty(propertyName).getType());
        }
        entityInstance.setPropertyVal(propertyName, newValue);
    }
}
