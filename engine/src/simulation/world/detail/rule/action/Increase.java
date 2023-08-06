package simulation.world.detail.rule.action;

import simulation.utils.Value;
import simulation.world.detail.entity.Entity;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;

public class Increase extends Action {
    double by = 0;

    public Increase(Entity entity, String propertyName, double by) {
        super(entity, propertyName);
        this.by = by;
    }

    @Override
    public void doAction() {
        increase();
    }
    private void increase() {
        try {
            double value = parseDouble(property.getValue().getCurrValue());
            value = value + by;
            property.setValue(new Value(Double.toString(value), property.getValue().isRandomInitialize()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
