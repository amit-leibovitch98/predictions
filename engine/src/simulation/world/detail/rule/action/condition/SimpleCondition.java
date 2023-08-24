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

    public SimpleCondition(Entity entity, String propertyName, CondOp operator, CondExpression value) {
        super(entity, propertyName);
        this.operator = operator;
        this.value = value;
    }
    @Override
    public void doAction(EntityInstance entityInstance) {
        evaluateCond(entityInstance);
    }

    public boolean evaluateCond(EntityInstance entityInstance) {
        switch (operator) {
            case EQUALS:
                if (entityInstance.getPropertyVal(propertyName) == value.resolveExpression()) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case NOT_EQUALS:
                if (entityInstance.getPropertyVal(propertyName) != value.resolveExpression()) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case BIGGER_THAN:
                if (compare(entityInstance.getPropertyVal(propertyName), value.resolveExpression())) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case LESSER_THAN:
                if (compare(value.resolveExpression(),entityInstance.getPropertyVal(propertyName))) {
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
            if(num1 instanceof Integer) {
                if(num2 instanceof Integer) {
                    return (int)num1 > (int)num2;
                } else if(num2 instanceof Float) {
                    return (int)num1 > (float)num2;
                } else {
                    throw new IllegalArgumentException("Cannot compare " + num1 + " and " + num2 );
                }
            } else if(num1 instanceof Float) {
                if(num2 instanceof Integer) {
                    return (float)num1 > (int)num2;
                } else if(num2 instanceof Float) {
                    return (float)num1 > (float)num2;
                } else {
                    throw new IllegalArgumentException("Cannot compare " + num1 + " and " + num2 );
                }
            } else {
                throw new IllegalArgumentException("Cannot compare " + num1 + " and " + num2 );
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot compare " + num1 + " and " + num2 );
        }

    }
}
