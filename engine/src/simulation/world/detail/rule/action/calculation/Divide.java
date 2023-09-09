package simulation.world.detail.rule.action.calculation;

import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;

public class Divide extends Calculation {
    public Divide(Entity entity, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(entity, resultProp, arg1, arg2);
    }

    @Override
    public void calculate(EntityInstance entityInstance) {
        try {
            float arg1 = getArgInFloat(entityInstance, this.arg1);
            float arg2 = getArgInFloat(entityInstance, this.arg2);

            float value = arg1 / arg2;
            if (resultProp.getType() == Type.DECIMAL) {
                entityInstance.setPropertyVal(resultProp.getName(), Math.round(value));
            } else {
                entityInstance.setPropertyVal(resultProp.getName(), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
