package simulation.world.detail.rule.action.calculation;

import simulation.utils.expression.CondExpression;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.action.Action;

public abstract class Calculation extends Action {
    EntityProperty resultProp;
    CondExpression arg1;
    CondExpression arg2;

    public Calculation(Entity entity, String resultProp, CondExpression arg1, CondExpression arg2) {
        super(entity, null);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.resultProp = entity.getProperty(resultProp);
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
