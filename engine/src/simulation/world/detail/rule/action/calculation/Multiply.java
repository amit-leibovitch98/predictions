package simulation.world.detail.rule.action.calculation;

import simulation.utils.Value;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;

import static java.lang.Double.parseDouble;

public class Multiply extends Calculation {
    public Multiply(Entity entity, String propertyName, float arg1, float arg2) {
        super(entity, propertyName, arg1, arg2);
    }

    @Override
    public void calculate(EntityInstance entityInstance) {
        try {
            double value = arg1 * arg2;
            entityInstance.setPropertyVal(propertyName, Double.toString(value));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
