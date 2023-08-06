package simulation.world.detail.rule.action.calculation;

import simulation.utils.Value;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.rule.action.Action;

import static java.lang.Double.parseDouble;

public class Divide extends Calculation {
    public Divide(Entity entity, String propertyName, double arg1, double arg2) {
        super(entity, propertyName, arg1, arg2);
    }

    @Override
    public void calculate() {
        try {
            double value = arg1 / arg2;
            resultProp.setValue(new Value(Double.toString(value), resultProp.getValue().isRandomInitialize()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
