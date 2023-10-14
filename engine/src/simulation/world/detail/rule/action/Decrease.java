package simulation.world.detail.rule.action;

import simulation.utils.Range;
import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.condition.Condition;


public class Decrease extends Action {
    private CondExpression by;

    public Decrease(Entity primeryentity, String propertyName, CondExpression by) {
        super(primeryentity, propertyName);
        this.by = by;
    }

    public Decrease(Entity primeryentity, Entity secondaryentity, int selectionCount, Condition selectionCond, String propertyName, CondExpression by) {
        super(primeryentity, secondaryentity, selectionCount, selectionCond, propertyName);
        this.by = by;
    }

    @Override
    public boolean doAction(EntityInstance primeryEntityInstance, EntityInstance secendaryEntityInstance) {
        return decrease(primeryEntityInstance, secendaryEntityInstance);
    }

    @Override
    public boolean doAction(EntityInstance entityInstance) {
        return decrease(entityInstance);
    }

    private boolean decrease(EntityInstance entityInstance) {
        Range propRange = entityInstance.getProperty(propertyName).getRange();
        Object byValue = by.resolveExpression(entityInstance);
        Object propertyVal = entityInstance.getPropertyVal(propertyName);
        Object oldValue = propertyVal;
        Object newValue = getNewValue(entityInstance, byValue, propertyVal, propRange);
        entityInstance.setPropertyVal(propertyName, newValue, false);
        return oldValue != newValue;
    }

    private boolean decrease(EntityInstance entityInstance, EntityInstance secendaryEntityInstance) {
        Range propRange = entityInstance.getProperty(propertyName).getRange();
        Object byValue = by.resolveExpression(entityInstance, secendaryEntityInstance);
        Object propertyVal = entityInstance.getPropertyVal(propertyName);
        Object newValue = getNewValue(entityInstance, byValue, propertyVal, propRange);
        entityInstance.setPropertyVal(propertyName, newValue, false);
        return propertyVal != newValue;
    }

    private Object getNewValue(EntityInstance entityInstance, Object byValue, Object propertyValue, Range propRange) {
        Object newValue = null;
        if (entityInstance.getProperty(propertyName).getType() == Type.DECIMAL) {
            if(byValue instanceof Integer) {
                newValue = (int) propertyValue - (int) byValue;
            } else if (byValue instanceof Float) {
                newValue = (int) propertyValue - Math.round((float) byValue);
            } else {
                throw new IllegalArgumentException("Cannot increase decimal property by non-number value");
            }
            if(propRange == null || (int) newValue <= propRange.getFrom()) {
                newValue = Math.round(entityInstance.getProperty(propertyName).getRange().getFrom());
            }
        } else if (entityInstance.getProperty(propertyName).getType() == Type.FLOAT) {
            if(byValue instanceof Integer) {
                newValue = (float) propertyValue - (int) byValue;
            } else if (byValue instanceof Float) {
                newValue = (float) propertyValue - (float) byValue;
            } else {
                throw new IllegalArgumentException("Cannot increase float property by non-number value");
            }
            if(propRange == null || (float) newValue <= propRange.getFrom()) {
                newValue = entityInstance.getProperty(propertyName).getRange().getFrom();
            }
        }
        return newValue;
    }
}
