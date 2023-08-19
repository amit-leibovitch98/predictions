package file;

import file.schema.generated.*;
import simulation.utils.Range;
import simulation.utils.Type;
import simulation.utils.Value;
import simulation.world.World;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Activation;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.TerminationCond;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.rule.action.*;
import simulation.world.detail.rule.action.Action;
import simulation.world.detail.rule.action.calculation.Calculation;
import simulation.world.detail.rule.action.calculation.Divide;
import simulation.world.detail.rule.action.calculation.Multiply;
import simulation.world.detail.rule.action.condition.*;

import simulation.utils.expression.CondExpression;

import javax.swing.*;
import java.beans.Expression;
import java.util.ArrayList;
import java.util.List;

public class WorldProxy {
    private FIleHandler fileHandler;
    private PRDWorld prdWorld;

    private List<EnvironmentVariable> envVars;

    public WorldProxy(String path) {
        FIleHandler fileHandler = new FIleHandler(path);
        this.prdWorld = fileHandler.getPrdWorld();
    }

    public World getWorld() {
        World world = new World();
        this.envVars = getEnvironmentVariableList();
        world.setEnvironmentVars(this.envVars);
        world.setEntities(getEntityList());
        world.setRules(getRuleList(world.getEntities().get(0)));  //Fixme: for ex2 well have more than 1 entity
        world.setTerminationConds(getTerminationList());
        return world;
    }

    private List<EnvironmentVariable> getEnvironmentVariableList() {
        List<EnvironmentVariable> environmentVariables = new ArrayList<EnvironmentVariable>();
        PRDEvironment prdEnvironments = prdWorld.getPRDEvironment();

        for (PRDEnvProperty prdEnvProperty : prdEnvironments.getPRDEnvProperty()) {
            PRDRange prdRange = prdEnvProperty.getPRDRange();
            environmentVariables.add(new EnvironmentVariable(
                    prdEnvProperty.getPRDName(),
                    new Range(prdRange.getFrom(), prdRange.getTo()),
                    Type.fromString(prdEnvProperty.getType())
            ));
        }
        return environmentVariables;
    }

    private List<Entity> getEntityList() {
        List<Entity> entities = new ArrayList<Entity>();
        PRDEntities prdEntities = prdWorld.getPRDEntities();
        for (PRDEntity prdEntity : prdEntities.getPRDEntity()) {
            entities.add(new Entity(
                    prdEntity.getName(),
                    prdEntity.getPRDPopulation(),
                    getEntityProperties(prdEntity.getPRDProperties())
            ));

        }

        return entities;
    }

    private List<Rule> getRuleList(Entity entity) {
        List<Rule> rules = new ArrayList<Rule>();
        PRDRules prdRules = prdWorld.getPRDRules();
        for (PRDRule prdRule : prdRules.getPRDRule()) {
            //create rule actions
            List<Action> actions = new ArrayList<Action>();
            for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
                actions.add(createAction(prdAction, entity));
            }
            //create rule activation
            Activation activation = null;
            if(prdRule.getPRDActivation() != null) {
                activation = new Activation(prdRule.getPRDActivation().getTicks(), prdRule.getPRDActivation().getProbability());
            } else {
                activation = new Activation();
            }
            //create rule
            rules.add(new Rule(prdRule.getName(), activation, actions));
        }
        return rules;
    }

    private TerminationCond getTerminationList() {
        Integer ticks = null, seconds = null;
        List<Object> termList = prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond();
        for(Object prdBySecond : termList) {
            if (prdBySecond.getClass() == PRDByTicks.class) {
                ticks = ((PRDByTicks)(prdBySecond)).getCount();
            } else if (prdBySecond.getClass() == PRDBySecond.class) {
                seconds = ((PRDBySecond)(prdBySecond)).getCount();
            }
        }
        return new TerminationCond(ticks, seconds);
    }

    private List<EntityProperty> getEntityProperties(PRDProperties prdProperties) {
        List<EntityProperty> properties = new ArrayList<EntityProperty>();
        for (PRDProperty prdProperty : prdProperties.getPRDProperty()) {
            PRDRange prdRange = prdProperty.getPRDRange();
            PRDValue prdValue = prdProperty.getPRDValue();
            Range range = new Range(prdRange.getFrom(), prdRange.getTo());
            properties.add(new EntityProperty(
                    prdProperty.getPRDName(),
                    range,
                    Type.fromString(prdProperty.getType()),
                    prdValue
            ));
        }
        return properties;
    }

    private Action createAction(PRDAction prdAction, Entity entity) {
        switch(ActionType.fromString(prdAction.getType())) {
            case INCREASE:
                return new Increase(entity,prdAction.getProperty(), new CondExpression(prdAction.getBy(), envVars));
            case DECREASE:
                return new Decrease(entity,prdAction.getProperty(), new CondExpression(prdAction.getBy(), envVars));
            case CALCULATION:
                return createCalculation(prdAction, entity);
            case CONDITION:
                return createConditions(prdAction, entity, getEnvironmentVariableList());
            case SET:
                return new Set(entity,prdAction.getProperty(), new CondExpression(prdAction.getBy(), envVars));
            case KILL:
                return new Kill(entity);
            default:
                throw new RuntimeException("Unknown action type");
        }
    }

    private Calculation createCalculation(PRDAction prdAction, Entity entity){
        Calculation calc = null;
        if(prdAction.getPRDDivide() != null) {
            calc = new Divide(entity, prdAction.getProperty(), Double.parseDouble(prdAction.getPRDDivide().getArg1()),
                    Double.parseDouble(prdAction.getPRDDivide().getArg2()));
        } else if (prdAction.getPRDMultiply() != null) {
            calc = new Multiply(entity, prdAction.getProperty(), Double.parseDouble(prdAction.getPRDMultiply().getArg1()),
                    Double.parseDouble(prdAction.getPRDMultiply().getArg2()));
        } else {
            throw new RuntimeException("Unknown calculation type");
        }
        return calc;
    }

    private Condition createConditions (PRDAction prdAction, Entity entity, List<EnvironmentVariable> envVars) {
        List<Action> thenActions = null, elseActions = null;
        //get then
        if(prdAction.getPRDThen() != null) {
            thenActions = getThenOrElseActions(prdAction.getPRDThen().getPRDAction(), entity);
        }
        //get else
        if(prdAction.getPRDElse() != null) {
            elseActions = getThenOrElseActions(prdAction.getPRDElse().getPRDAction(), entity);
        }
        //get condition
        Condition condition = getCondList(prdAction.getPRDCondition(), entity, envVars);
        condition.setThenActions(thenActions);
        condition.setElseActions(elseActions);
        return condition;
    }
    private Condition getCondList(PRDCondition prdCond, Entity entity, List<EnvironmentVariable> envVars) {
        if(prdCond.getSingularity().equals("single")) {
            return new SimpleCondition(entity,
                    prdCond.getProperty(),
                    CondOp.fromString(prdCond.getOperator()),
                    new CondExpression(prdCond.getValue(), envVars));
        } else if (prdCond.getSingularity().equals("multiple")) {
            List<ICond> conditions = new ArrayList<ICond>();
            for(PRDCondition condition : prdCond.getPRDCondition()) {
                conditions.add(getCondList(condition, entity, envVars));
            }
            return new ComplexCondition(conditions,
                    prdCond.getLogical(),
                    entity,
                    prdCond.getProperty());
        }
        else {
            throw new RuntimeException("Unknown condition type" + prdCond.getSingularity());
        }
    }
    private List<Action> getThenOrElseActions(List<PRDAction> thenOrrElseActions, Entity entity) {
        List<Action> result = null;
        result = new ArrayList<Action>();
        for (PRDAction thenOrrElseAction : thenOrrElseActions) {
            result.add(createAction(thenOrrElseAction, entity));
        }
        return result;
    }
}
