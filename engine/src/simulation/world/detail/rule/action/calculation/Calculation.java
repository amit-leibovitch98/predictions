package simulation.world.detail.rule.action.calculation;

import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.action.Action;
import simulation.world.detail.rule.action.condition.Condition;

public abstract class Calculation extends Action {
    EntityProperty resultProp;
    CondExpression arg1;
    CondExpression arg2;

    public Calculation(Entity primeryEntity, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(primeryEntity, null);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.resultProp = primeryEntity.getProperty(resultProp);
    }

    public Calculation(Entity primeryEntity, Entity secenderyEntity, int selectionCount, Condition selectionCond, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(primeryEntity, secenderyEntity, selectionCount, selectionCond, null);
        this.arg1 = arg1;
        this.resultProp = primeryEntity.getProperty(resultProp);
    }

    public abstract void calculate(EntityInstance entityInstance);

    protected float getArgInFloat(EntityInstance entityInstance, CondExpression arg) {
        if (arg.resolveExpression(entityInstance) instanceof Integer) {
            return ((int) arg.resolveExpression(entityInstance));
        } else {
            return (float) (arg.resolveExpression(entityInstance));
        }
    }

    public boolean doAction(EntityInstance entityInstance) {
        calculate(entityInstance);
        return true;
    }
    @Override
    public boolean doAction(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance) {
        throw new UnsupportedOperationException("Set action doesn't support doAction with two entity instances");
    }

}
