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
                    activateThen();
                    return true;
                } else {
                    activateElse();
                    return false;
                }
            case NOT_EQUALS:
                if (entityInstance.getPropertyVal(propertyName) != value.resolveExpression()) {
                    activateThen();
                    return true;
                } else {
                    activateElse();
                    return false;
                }
            case BIGGER_THAN:
                if (compare(entityInstance.getPropertyVal(propertyName), value.resolveExpression(), property.getType())) {
                    activateThen();
                    return true;
                } else {
                    activateElse();
                    return false;
                }
            case LESSER_THAN:
                if (!compare(entityInstance.getPropertyVal(propertyName), value.resolveExpression(), property.getType())) {
                    activateThen();
                    return true;
                } else {
                    activateElse();
                    return false;
                }
            default:
                throw new IllegalArgumentException("Unknown expression type: " + operator);
        }
    }


    private boolean compare(Object num1, Object num2, Type type) {
        if(type == Type.DECIMAL) {
            return (double)num1 > (double)num2;
        } else if(type == Type.FLOAT) {
            return (float)num1 > (float)num2;
        } else {
            throw new RuntimeException("Cannot compare non-number property with bigger than operator");
        }

    }

    public void activateThen() {
        //TODO: activate then
    }

    public void activateElse() {
        //TODO: activate else
    }

}
