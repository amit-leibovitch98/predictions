package simulation.world.detail.rule.action.condition;

import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.action.Action;

import java.util.List;

public class SimpleCondition extends Condition implements ICond {
    CondOp operator;
    CondExpression value;
    CondExpression propertyExpression;

    public SimpleCondition(Entity entity, CondExpression propertyExpression, CondOp operator, CondExpression value) {
        super(entity, null);
        this.operator = operator;
        this.value = value;
        this.propertyExpression = propertyExpression;
    }
    @Override
    public void doAction(EntityInstance entityInstance) {
        evaluateCond(entityInstance);
    }

    public boolean evaluateCond(EntityInstance entityInstance) {
        switch (operator) {
            case EQUALS:
                if (propertyExpression.resolveExpression(entityInstance) == value.resolveExpression(entityInstance)) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case NOT_EQUALS:
                if (propertyExpression.resolveExpression(entityInstance) != value.resolveExpression(entityInstance)) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case BIGGER_THAN:
                if (compare(propertyExpression.resolveExpression(entityInstance), value.resolveExpression(entityInstance))) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case LESSER_THAN:
                if (compare(value.resolveExpression(entityInstance),propertyExpression.resolveExpression(entityInstance))) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            default:
                throw new IllegalArgumentException("Unknown expression type: " + operator);
        }
    }


    private boolean compare(Object num1, Object num2) {
        try {
            float num1Float = (float) num1;
            float num2Float = (float) num2;
            return num1Float > num2Float;
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't compare " + num1 + " and " + num2);
        }
    }
}
