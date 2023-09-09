package simulation.world.detail.rule.action;

import simulation.utils.Range;
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
    public boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance) {
        throw new UnsupportedOperationException("Set action doesn't support doAction with two entity instances");
    }
    public boolean doAction(EntityInstance entityInstance) {
        return increase(entityInstance);
    }

    private boolean increase(EntityInstance entityInstance) {
        boolean res = true;
        Range propRange = entityInstance.getProperty(propertyName).getRange();
        Object newValue = null;
        Object byValue = by.resolveExpression(entityInstance);
        Object ptopValue = entityInstance.getPropertyVal(propertyName);
        if (entityInstance.getProperty(propertyName).getType() == Type.DECIMAL) {
            if(byValue instanceof Integer) {
                newValue = (int) ptopValue + (int) byValue;
            } else if (byValue instanceof Float) {
                newValue = (int) ptopValue + Math.round((float) byValue);
            } else {
                throw new IllegalArgumentException("Cannot increase decimal property by non-number value");
            }
            if(propRange == null || (int) newValue <= propRange.getTo()) {
                newValue = Math.round(entityInstance.getProperty(propertyName).getRange().getTo());
                res = false; //value didn't change
            }
        } else if (entityInstance.getProperty(propertyName).getType() == Type.FLOAT) {
            if(byValue instanceof Integer) {
                newValue = (float) ptopValue + (float) byValue;
            } else if (byValue instanceof Float) {
                newValue = (float) ptopValue + (float) byValue;
            } else {
                throw new IllegalArgumentException("Cannot increase float property by non-number value");
            }
            if(propRange == null || (float) newValue <= propRange.getTo()) {
                newValue = entityInstance.getProperty(propertyName).getRange().getTo();
                res = false; //value didn't change
            }
        }
        entityInstance.setPropertyVal(propertyName, newValue);
        return res;
    }

}
