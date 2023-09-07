package simulation.world.detail.rule.action.calculation;

import simulation.utils.Type;
import simulation.utils.Value;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;

import static java.lang.Double.TYPE;
import static java.lang.Double.parseDouble;

public class Multiply extends Calculation {
    public Multiply(Entity entity, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(entity, resultProp, arg1, arg2);
    }

    @Override
    public void calculate(EntityInstance entityInstance) {
        float arg1, arg2;
        try {
            if (this.arg1.resolveExpression(entityInstance) instanceof Integer) {
                arg1 = ((int) this.arg1.resolveExpression(entityInstance));
            } else {
                arg1 = (float) (this.arg1.resolveExpression(entityInstance));
            }
            if (this.arg2.resolveExpression(entityInstance) instanceof Integer) {
                arg2 = ((int) this.arg2.resolveExpression(entityInstance));
            } else {
                arg2 = (float) (this.arg2.resolveExpression(entityInstance));
            }
            float value = arg1 * arg2;
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
