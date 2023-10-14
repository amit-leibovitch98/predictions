package simulation.world.detail.rule.action.calculation;

import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;
import simulation.world.detail.rule.action.condition.Condition;

import static java.lang.Double.TYPE;
import static java.lang.Double.parseDouble;

public class Multiply extends Calculation {
    public Multiply(Entity primeryEntity, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(primeryEntity, resultProp, arg1, arg2);
    }

    public Multiply(Entity primeryEntity, Entity secenderyEntity, int selectionCount, Condition selectionCond, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(primeryEntity, secenderyEntity, selectionCount, selectionCond, resultProp, arg1, arg2);
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
                entityInstance.setPropertyVal(resultProp.getName(), Math.round(value), false);
            } else {
                entityInstance.setPropertyVal(resultProp.getName(), value, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
