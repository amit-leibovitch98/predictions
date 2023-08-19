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
        Object newValue = null;
        Object byValue = by.resolveExpression();
        Object ptopValue = entityInstance.getPropertyVal(propertyName);
        if (entityInstance.getProperty(propertyName).getType() == Type.DECIMAL) {
            if(byValue instanceof Integer) {
                newValue = (int) ptopValue + (int) byValue;
            } else if (byValue instanceof Float) {
                newValue = (int) ptopValue + Math.round((float) byValue);
            } else {
                throw new IllegalArgumentException("Cannot increase decimal property by non-number value");
            }
            if((int) newValue > entityInstance.getProperty(propertyName).getRange().getTo()) {
                newValue = Math.round(entityInstance.getProperty(propertyName).getRange().getTo());
            }
        } else if (entityInstance.getProperty(propertyName).getType() == Type.FLOAT) {
            if(byValue instanceof Integer) {
                newValue = (float) ptopValue + (float) byValue;
            } else if (byValue instanceof Float) {
                newValue = (float) ptopValue + (float) byValue;
            } else {
                throw new IllegalArgumentException("Cannot increase float property by non-number value");
            }
            if((float) newValue > entityInstance.getProperty(propertyName).getRange().getTo()) {
                newValue = entityInstance.getProperty(propertyName).getRange().getTo();
            }
        }
        entityInstance.setPropertyVal(propertyName, newValue);
    }

}
