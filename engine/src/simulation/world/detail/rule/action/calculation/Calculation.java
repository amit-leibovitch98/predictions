package simulation.world.detail.rule.action.calculation;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.action.Action;

public abstract class Calculation extends Action {
    EntityProperty resultProp = null;
    double arg1 = 0;
    double arg2 = 0;

    public Calculation(Entity entity, String propertyName, double arg1, double arg2) {
        super(entity, propertyName);
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public abstract void calculate();

    public void doAction() {
        calculate();
    }
}
