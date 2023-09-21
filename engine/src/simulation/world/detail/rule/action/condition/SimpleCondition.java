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
    CondExpression property;

    public SimpleCondition(Entity primeryentity, CondExpression property, CondOp operator, CondExpression value) {
        super(primeryentity, null);
        this.operator = operator;
        this.value = value;
        this.property = property;
    }

    public SimpleCondition(Entity primeryentity, Entity secenderyEntity, int selectionCount, Condition selectionCond, CondExpression property, CondOp operator, CondExpression value) {
        super(primeryentity, secenderyEntity, selectionCount, selectionCond, null);
        this.operator = operator;
        this.value = value;
        this.property = property;
    }

    @Override
    public boolean doAction(EntityInstance entityInstance) {
        return evaluateCond(entityInstance);
    }

    @Override
    public boolean doAction(EntityInstance entityInstance, EntityInstance secondaryEntityInstance) {
        return evaluateCond(entityInstance);
    }

    public boolean evaluateCond(EntityInstance entityInstance) {
        switch (operator) {
            case EQUALS:
                if (property.resolveExpression(entityInstance) == value.resolveExpression(entityInstance)) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case NOT_EQUALS:
                if (property.resolveExpression(entityInstance) != value.resolveExpression(entityInstance)) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case BIGGER_THAN:
                if (compare(property.resolveExpression(entityInstance), value.resolveExpression(entityInstance))) {
                    super.activateThen(entityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance);
                    return false;
                }
            case LESSER_THAN:
                if (compare(property.resolveExpression(entityInstance) ,entityInstance.getPropertyVal(this.property.resolveExpression(entityInstance).toString()))) {
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

    public boolean evaluateCond(EntityInstance entityInstance, EntityInstance secondaryEntityInstance) {
        switch (operator) {
            case EQUALS:
                if (property.resolveExpression(entityInstance) == value.resolveExpression(entityInstance)) {
                    super.activateThen(entityInstance, secondaryEntityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance, secondaryEntityInstance);
                    return false;
                }
            case NOT_EQUALS:
                if (property.resolveExpression(entityInstance) != value.resolveExpression(entityInstance)) {
                    super.activateThen(entityInstance, secondaryEntityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance, secondaryEntityInstance);
                    return false;
                }
            case BIGGER_THAN:
                if (compare(property.resolveExpression(entityInstance), value.resolveExpression(entityInstance))) {
                    super.activateThen(entityInstance, secondaryEntityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance, secondaryEntityInstance);
                    return false;
                }
            case LESSER_THAN:
                if (compare(property.resolveExpression(entityInstance) ,entityInstance.getPropertyVal(this.property.resolveExpression(entityInstance).toString()))) {
                    super.activateThen(entityInstance, secondaryEntityInstance);
                    return true;
                } else {
                    super.activateElse(entityInstance, secondaryEntityInstance);
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
