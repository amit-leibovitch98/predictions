package simulation.world.detail.rule.action.condition;

import simulation.utils.expression.Expression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.rule.action.Action;

public class Condition extends Action {
    private boolean isSimple;
    CondOp operator;
    Expression value;

    Condition(Entity entity, String propertyName, String operator, String value) {
        super(entity, propertyName);
        this.operator = CondOp.fromString(operator);
        this.value = new Expression(value);
        this.isSimple = true;
    }
    @Override
    public void doAction() {
        switch (operator) {
            case EQUALS:
                if (property.getValue().getCurrValue() == value.resolveExpression()) {
                    activateThen();
                } else {
                    activateElse();
                }
                break;
            case NOT_EQUALS:
                if (property.getValue().getCurrValue() != value.resolveExpression()) {
                    activateThen();
                } else {
                    activateElse();
                }
                break;
            case BIGGER_THAN:
                if (property.getValue().getCurrValue() > value.resolveExpression()) {
                    activateThen();
                } else {
                    activateElse();
                }
                break;
            case LESSER_THAN:
                if (property.getValue().getCurrValue() < value.resolveExpression()) {
                    activateThen();
                } else {
                    activateElse();
                }
                break;
        }
    }

    private void activateThen() {
        //TODO: activate then
    }

    private void activateElse() {
        //TODO: activate else
    }

}
