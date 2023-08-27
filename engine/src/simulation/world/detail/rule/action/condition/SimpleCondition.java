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
            float num1Float = (float) num1;
            float num2Float = (float) num2;
            return num1Float > num2Float;
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't compare " + num1 + " and " + num2);
        }
    }
}
