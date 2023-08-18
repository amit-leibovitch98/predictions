package simulation.world.detail.rule.action;

import simulation.utils.Type;
import simulation.utils.Value;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;

import java.beans.Expression;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;

public class Decrease extends Action {
    private CondExpression by;

    public Decrease(Entity entity, String propertyName, CondExpression by) {
        super(entity, propertyName);
        this.by = by;
    }

    @Override
    public void doAction() {
        decrease();
    }
    public void decrease() {
        if (property.getType() == Type.FLOAT) {
            double value = (double) (property.getValue().getCurrValue());
            property.getValue().setValue(value - (double)by.resolveExpression());
        } else if (property.getType() == Type.DECIMAL) {
            double value = (int) (property.getValue().getCurrValue());
            property.getValue().setValue(value - (double) by.resolveExpression());
        } else {
            throw new RuntimeException("Cannot decrease non-number property");
        }
    }
}
