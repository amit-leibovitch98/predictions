package simulation.world.detail.rule.action;

import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;



public class Increase extends Action {
    private CondExpression by;

    public Increase(Entity entity, String propertyName, CondExpression by) {
        super(entity, propertyName);
        this.by = by;
    }

    @Override
    public void doAction() {
        increase();
    }
    private void increase() {
        if (property.getType() == Type.FLOAT) {
            double value = (double) (property.getValue().getCurrValue());
            property.getValue().setValue(value + (double) by.resolveExpression());
        } else if (property.getType() == Type.DECIMAL) {
            double value = (int) (property.getValue().getCurrValue());
            property.getValue().setValue(value + (double) by.resolveExpression());
        } else {
            throw new RuntimeException("Cannot decrease non-number property");
        }
    }
}
