package simulation.world.detail.rule.action.condition;

import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.rule.action.Action;

import java.util.List;

public class ComplexCondition extends Condition implements ICond {
    List<ICond> subConditions;
    ConditionLogicOp logicOp;

    public ComplexCondition(List<ICond> simpleConditions, Entity primeryentity, String logicOp) {
        super(primeryentity, null);
        this.subConditions = simpleConditions;
        this.logicOp = ConditionLogicOp.fromString(logicOp);
    }

    public ComplexCondition(List<ICond> simpleConditions, Entity primeryentity, Entity secondaryentity, int selectionCount, Condition selectionCond, String logicOp) {
        super(primeryentity, secondaryentity, selectionCount, selectionCond, null);
        this.subConditions = simpleConditions;
        this.logicOp = ConditionLogicOp.fromString(logicOp);
    }

    public boolean evaluateCond(EntityInstance entityInstance){
        if(logicOp == ConditionLogicOp.AND){
            return evaluateAnd(entityInstance);
        } else if(logicOp == ConditionLogicOp.OR){
            return evaluateOr(entityInstance);
        } else {
            throw new IllegalArgumentException("Unknown expression type: " + logicOp);
        }
    }

    private boolean evaluateAnd(EntityInstance entityInstance){
        boolean result = true;
        for(ICond cond : subConditions){
            result = result && cond.evaluateCond(entityInstance);
        }
        return result;
    }

    private boolean evaluateOr(EntityInstance entityInstance) {
        boolean result = false;
        for(ICond cond : subConditions){
            result = result || cond.evaluateCond(entityInstance);
        }
        return result;
    }


}