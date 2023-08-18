package simulation.world.detail.rule.action.condition;

import simulation.world.detail.entity.Entity;
import simulation.world.detail.rule.action.Action;

import java.util.List;

public class ComplexCondition extends Condition implements ICond {
    List<ICond> subConditions;
    ConditionLogicOp logicOp;

    public ComplexCondition(List<ICond> simpleConditions, String logicOp, Entity entity, String propertyName) {
        super(entity, propertyName);
        this.subConditions = simpleConditions;
        this.logicOp = ConditionLogicOp.fromString(logicOp);
    }

    public boolean evaluateCond(){
        if(logicOp == ConditionLogicOp.AND){
            return evaluateAnd();
        } else if(logicOp == ConditionLogicOp.OR){
            return evaluateOr();
        } else {
            throw new IllegalArgumentException("Unknown expression type: " + logicOp);
        }
    }

    private boolean evaluateAnd(){
        boolean result = true;
        for(ICond cond : subConditions){
            result = result && cond.evaluateCond();
        }
        return result;
    }

    private boolean evaluateOr(){
        boolean result = false;
        for(ICond cond : subConditions){
            result = result || cond.evaluateCond();
        }
        return result;
    }
}