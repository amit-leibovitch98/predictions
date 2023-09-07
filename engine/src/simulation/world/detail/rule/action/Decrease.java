package simulation.world.detail.rule.action;

import simulation.utils.Range;
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
        Range propRange = entityInstance.getProperty(propertyName).getRange();
        Object newValue = null;
        Object byValue = by.resolveExpression(entityInstance);
        Object ptopValue = entityInstance.getPropertyVal(propertyName);
        if (entityInstance.getProperty(propertyName).getType() == Type.DECIMAL) {
            if(byValue instanceof Integer) {
                newValue = (int) ptopValue - (int) byValue;
            } else if (byValue instanceof Float) {
                newValue = (int) ptopValue - Math.round((float) byValue);
            } else {
                throw new IllegalArgumentException("Cannot increase decimal property by non-number value");
            }
            if(propRange == null || (int) newValue >= propRange.getFrom()) {
                newValue = Math.round(entityInstance.getProperty(propertyName).getRange().getTo());
            }
        } else if (entityInstance.getProperty(propertyName).getType() == Type.FLOAT) {
            if(byValue instanceof Integer) {
                newValue = (float) ptopValue - (float) byValue;
            } else if (byValue instanceof Float) {
                newValue = (float) ptopValue - (float) byValue;
            } else {
                throw new IllegalArgumentException("Cannot increase float property by non-number value");
            }
            if(propRange == null || (float) newValue >= propRange.getFrom()) {
                newValue = entityInstance.getProperty(propertyName).getRange().getFrom();
            }
        }
        entityInstance.setPropertyVal(propertyName, newValue);
    }
}
