package simulation.world.detail.rule.action;

import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;


public class Increase extends Action {
    private CondExpression by;

    public Increase(Entity entity, String propertyName, CondExpression by) {
        super(entity, propertyName);
        this.by = by;
    }

    @Override
    public void doAction(EntityInstance entityInstance) {
        increase(entityInstance);
    }
    private void increase(EntityInstance entityInstance) {
        if (entityInstance.getProperty(propertyName).getType() == Type.FLOAT) {
            double value = (double) (entityInstance.getPropertyVal(propertyName));
            entityInstance.setPropertyVal(propertyName, (value + (double) by.resolveExpression()));
        } else if (entityInstance.getProperty(propertyName).getType() == Type.DECIMAL) {
            double value = (int) (entityInstance.getPropertyVal(propertyName));
            entityInstance.setPropertyVal(propertyName, (value + (double) by.resolveExpression()));
        } else {
            throw new RuntimeException("Cannot decrease non-number property");
        }
    }
}
