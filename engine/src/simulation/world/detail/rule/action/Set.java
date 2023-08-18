package simulation.world.detail.rule.action;

import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;

import java.beans.Expression;

public class Set extends Action {
    private final CondExpression value;

    public Set(Entity entity, String propertyName, CondExpression value) {
        super(entity, propertyName);
        this.value = value;
    }
    @Override
    public void doAction() {
        property.getValue().setValue(value);

    }
}
