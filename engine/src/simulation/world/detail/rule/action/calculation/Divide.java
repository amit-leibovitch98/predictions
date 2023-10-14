package simulation.world.detail.rule.action.calculation;

import simulation.utils.Type;
import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.condition.Condition;

public class Divide extends Calculation {
    public Divide(Entity primeryEntity, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(primeryEntity, resultProp, arg1, arg2);
    }

    public Divide(Entity primeryEntity, Entity secenderyEntity, int selectionCount, Condition selectionCond, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(primeryEntity, secenderyEntity, selectionCount, selectionCond, resultProp, arg1, arg2);
    }

    @Override
    public void calculate(EntityInstance entityInstance) {
        try {
            float arg1 = getArgInFloat(entityInstance, this.arg1);
            float arg2 = getArgInFloat(entityInstance, this.arg2);

            float value = arg1 / arg2;
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
