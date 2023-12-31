package file;

import file.schema.generated.*;
import simulation.SimulationManager;
import simulation.utils.Grid;
import simulation.utils.Range;
import simulation.utils.Type;
import simulation.world.WorldDef;
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

import java.util.ArrayList;
import java.util.List;


public class WorldProxy {
    private PRDWorld prdWorld;
    private List<Entity> entities;
    private List<EnvironmentVariable> envVars;
    private List<Action> actions;

    public WorldProxy(String path) {
        FIleHandler fileHandler = new FIleHandler(path);
        this.prdWorld = fileHandler.getPrdWorld();
        SimulationManager.getInstance().getSimulationExecutionManager().setThreadQueue(getThreadsNum());
        actions = new ArrayList<>();
    }

    public WorldDef getWorld() {
        this.envVars = getEnvironmentVariableList();
        this.entities = getEntityList();
        WorldDef worldDef = new WorldDef(this.envVars, this.entities, getGrid(), this.actions);
        worldDef.setRules(getRuleList());
        worldDef.setTerminationConds(getTerminationList());
        return worldDef;
    }

    private int getThreadsNum() {
        return prdWorld.getPRDThreadCount();
    }

    private Grid getGrid() {
        PRDWorld.PRDGrid prdGrid = prdWorld.getPRDGrid();
        int rows = prdGrid.getRows();
        int columns = prdGrid.getColumns();
        if (rows < 10 || columns < 10 || rows > 100 || columns > 100) {
            throw new RuntimeException("Grid size must be between 10 and 100");
        }
        return new Grid(prdGrid.getRows(), prdGrid.getColumns());
    }

    private List<EnvironmentVariable> getEnvironmentVariableList() {
        List<EnvironmentVariable> environmentVariables = new ArrayList<EnvironmentVariable>();
        PRDEnvironment prdEnvironments = prdWorld.getPRDEnvironment();

        for (PRDEnvProperty prdEnvProperty : prdEnvironments.getPRDEnvProperty()) {
            PRDRange prdRange = prdEnvProperty.getPRDRange();
            Range range;
            if (prdRange == null) {
                range = null;
            } else {
                range = new Range(prdRange.getFrom(), prdRange.getTo());
            }

            environmentVariables.add(new EnvironmentVariable(
                    prdEnvProperty.getPRDName(),
                    range,
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
                    getEntityProperties(prdEntity.getPRDProperties())
            ));

        }

        return entities;
    }

    private List<Rule> getRuleList() {
        List<Rule> rules = new ArrayList<Rule>();
        PRDRules prdRules = prdWorld.getPRDRules();
        for (PRDRule prdRule : prdRules.getPRDRule()) {
            //create rule actions
            List<Action> actions = new ArrayList<>();
            for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
                actions.add(createAction(prdAction));
            }
            //create rule activation
            Activation activation = null;
            if (prdRule.getPRDActivation() != null) {
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
        boolean isInteractive = false;
        Integer ticks = null, seconds = null;
        List<Object> termList = prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks();
        for (Object prdBySecond : termList) {
            if (prdBySecond.getClass() == PRDByTicks.class) {
                ticks = ((PRDByTicks) (prdBySecond)).getCount();
            } else if (prdBySecond.getClass() == PRDBySecond.class) {
                seconds = ((PRDBySecond) (prdBySecond)).getCount();
            }
        }
        if (prdWorld.getPRDTermination().getPRDByUser() != null) {
            isInteractive = true;
        }
        return new TerminationCond(ticks, seconds, isInteractive);
    }

    private List<EntityProperty> getEntityProperties(PRDProperties prdProperties) {
        List<EntityProperty> properties = new ArrayList<EntityProperty>();
        for (PRDProperty prdProperty : prdProperties.getPRDProperty()) {
            PRDRange prdRange = prdProperty.getPRDRange();
            String prdValue = null;
            prdValue = prdProperty.getPRDValue().getInit();
            Range range;
            if (prdRange == null) {
                range = null;
            } else {
                range = new Range(prdRange.getFrom(), prdRange.getTo());
            }
            properties.add(new EntityProperty(
                    prdProperty.getPRDName(),
                    range,
                    Type.fromString(prdProperty.getType()),
                    prdValue
            ));
        }
        return properties;
    }

    private Action createAction(PRDAction prdAction) {
        try {
            Action action;
            if (prdAction.getType().equals(ActionType.REPLACE.toString())) {
                action = createReplaceAction(prdAction);
            } else if (prdAction.getType().equals(ActionType.PROXIMITY.toString())) {
                action = createProximityAction(prdAction);
            } else {
                action = createSimpleAction(prdAction);
            }
            actions.add(action);
            return action;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create action: " + e.getMessage());
        }
    }

    private Action createReplaceAction(PRDAction prdAction) {
        Entity killEntity = null;
        Entity replaceEntity = null;
        for (Entity currEntity : this.entities) {
            if (currEntity.getName().equals(prdAction.getKill())) {
                killEntity = currEntity;
            }
            if (currEntity.getName().equals(prdAction.getCreate())) {
                replaceEntity = currEntity;
            }
        }
        if (killEntity == null) {
            throw new RuntimeException("Unknown entity name to kill " + prdAction.getKill());
        } else if (replaceEntity == null) {
            throw new RuntimeException("Unknown entity name to create " + prdAction.getCreate());
        }

        return new Replace(killEntity, replaceEntity, prdAction.getMode().equals("scratch"));
    }

    private Action createProximityAction(PRDAction prdAction) {
        Entity sourceEntity = null;
        Entity targetEntity = null;
        for (Entity currEntity : this.entities) {
            if (currEntity.getName().equals(prdAction.getPRDBetween().getSourceEntity())) {
                sourceEntity = currEntity;
            }
            if (currEntity.getName().equals(prdAction.getPRDBetween().getTargetEntity())) {
                targetEntity = currEntity;
            }
        }
        if (sourceEntity == null || targetEntity == null) {
            throw new RuntimeException("Unknown entity name");
        }

        List<Action> subActions = new ArrayList<>();
        for (PRDAction subAction : prdAction.getPRDActions().getPRDAction()) {
            subActions.add(createAction(subAction));
        }
        return new Proximity(sourceEntity, targetEntity, new CondExpression(prdAction.getPRDEnvDepth().getOf(), this.envVars, this.entities), subActions);
    }

    private Action createSimpleActionWithSecendaryEntity(PRDAction prdAction) {
        Entity primeryEntity = null;
        for (Entity currEntity : this.entities) {
            if (currEntity.getName().equals(prdAction.getEntity())) {
                primeryEntity = currEntity;
            }
        }

        Entity secenderyEntity = null;
        for (Entity currEntity : this.entities) {
            if (currEntity.getName().equals(prdAction.getPRDSecondaryEntity().getEntity())) {
                secenderyEntity = currEntity;
            }
        }

        PRDAction.PRDSecondaryEntity.PRDSelection prdSelection = prdAction.getPRDSecondaryEntity().getPRDSelection();
        switch (ActionType.fromString(prdAction.getType())) {
            case INCREASE:
                return new Increase(primeryEntity, secenderyEntity, Integer.parseInt(prdSelection.getCount()),
                        getCondList(prdSelection.getPRDCondition(), primeryEntity, secenderyEntity),
                        prdAction.getProperty(),
                        new CondExpression(prdAction.getBy(), envVars, entities));
            case DECREASE:
                return new Decrease(primeryEntity, secenderyEntity, Integer.parseInt(prdSelection.getCount()),
                        createConditions(prdAction, secenderyEntity, primeryEntity, this.envVars),
                        prdAction.getProperty(),
                        new CondExpression(prdAction.getBy(), envVars, entities));
            case CALCULATION:
                return createCalculation(prdAction, primeryEntity, secenderyEntity);
            case CONDITION:
                return createConditions(prdAction, primeryEntity, secenderyEntity, getEnvironmentVariableList());
            case SET:
                return new Set(primeryEntity, secenderyEntity, Integer.parseInt(prdSelection.getCount()),
                        getCondList(prdSelection.getPRDCondition(), primeryEntity, secenderyEntity), prdAction.getProperty(),
                        new CondExpression(prdAction.getValue(), envVars, entities));
            case KILL:
                return new Kill(primeryEntity, secenderyEntity, Integer.parseInt(prdSelection.getCount()),
                        getCondList(prdSelection.getPRDCondition(), primeryEntity, secenderyEntity));
            default:
                throw new RuntimeException("Unknown action type");
        }
    }

    private Action createSimpleAction(PRDAction prdAction) {
        if (prdAction.getPRDSecondaryEntity() != null) {
            return createSimpleActionWithSecendaryEntity(prdAction);
        }

        Entity primeryEntity = null;
        for (Entity currEntity : this.entities) {
            if (currEntity.getName().equals(prdAction.getEntity())) {
                primeryEntity = currEntity;
            }
        }

        switch (ActionType.fromString(prdAction.getType())) {
            case INCREASE:
                return new Increase(primeryEntity, prdAction.getProperty(), new CondExpression(prdAction.getBy(), envVars, entities));
            case DECREASE:
                return new Decrease(primeryEntity, prdAction.getProperty(), new CondExpression(prdAction.getBy(), envVars, entities));
            case CALCULATION:
                return createCalculation(prdAction, primeryEntity);
            case CONDITION:
                return createConditions(prdAction, primeryEntity, null, getEnvironmentVariableList());
            case SET:
                return new Set(primeryEntity, prdAction.getProperty(), new CondExpression(prdAction.getValue(), envVars, entities));
            case KILL:
                return new Kill(primeryEntity);
            default:
                throw new RuntimeException("Unknown action type");
        }

    }

    private Calculation createCalculation(PRDAction prdAction, Entity primeryEntity, Entity secendeyEntity) {
        PRDAction.PRDSecondaryEntity.PRDSelection prdSelection = prdAction.getPRDSecondaryEntity().getPRDSelection();
        Calculation calc = null;
        if (prdAction.getPRDDivide() != null) {
            calc = new Divide(primeryEntity, secendeyEntity, Integer.parseInt(prdSelection.getCount()),
                    getCondList(prdSelection.getPRDCondition(), primeryEntity, secendeyEntity),
                    prdAction.getResultProp(),
                    new CondExpression(prdAction.getPRDDivide().getArg1(), this.envVars, this.entities),
                    new CondExpression(prdAction.getPRDDivide().getArg2(), this.envVars, this.entities));
        } else if (prdAction.getPRDMultiply() != null) {
            calc = new Multiply(primeryEntity, secendeyEntity, Integer.parseInt(prdSelection.getCount()),
                    getCondList(prdSelection.getPRDCondition(), primeryEntity, secendeyEntity),
                    prdAction.getResultProp(),
                    new CondExpression(prdAction.getPRDMultiply().getArg1(), this.envVars, this.entities),
                    new CondExpression(prdAction.getPRDMultiply().getArg2(), this.envVars, this.entities));
        } else {
            throw new RuntimeException("Unknown calculation type");
        }
        return calc;
    }

    private Calculation createCalculation(PRDAction prdAction, Entity entity) {
        Calculation calc = null;
        if (prdAction.getPRDDivide() != null) {
            calc = new Divide(entity, prdAction.getResultProp(),
                    new CondExpression(prdAction.getPRDDivide().getArg1(), this.envVars, this.entities),
                    new CondExpression(prdAction.getPRDDivide().getArg2(), this.envVars, this.entities));
        } else if (prdAction.getPRDMultiply() != null) {
            calc = new Multiply(entity, prdAction.getResultProp(),
                    new CondExpression(prdAction.getPRDMultiply().getArg1(), this.envVars, this.entities),
                    new CondExpression(prdAction.getPRDMultiply().getArg2(), this.envVars, this.entities));
        } else {
            throw new RuntimeException("Unknown calculation type");
        }
        return calc;
    }

    private Condition createConditions(PRDAction prdAction, Entity primeryEntity, Entity secendaryEntity, List<EnvironmentVariable> envVars) {
        List<Action> thenActions = null, elseActions = null;
        //get then
        if (prdAction.getPRDThen() != null) {
            thenActions = getThenOrElseActions(prdAction.getPRDThen().getPRDAction(), primeryEntity);
        }
        //get else
        if (prdAction.getPRDElse() != null) {
            elseActions = getThenOrElseActions(prdAction.getPRDElse().getPRDAction(), primeryEntity);
        }
        //get condition
        Condition condition = getCondList(prdAction.getPRDCondition(), primeryEntity, secendaryEntity);
        condition.setThenActions(thenActions);
        condition.setElseActions(elseActions);
        if(prdAction.getPRDSecondaryEntity() != null) {
            condition.setSecenderyEntitySelection(secendaryEntity,
                    Integer.parseInt(prdAction.getPRDSecondaryEntity().getPRDSelection().getCount()),
                    getCondList(prdAction.getPRDSecondaryEntity().getPRDSelection().getPRDCondition(), primeryEntity, secendaryEntity));
        }

        return condition;
    }

    private Condition getCondList(PRDCondition prdCond, Entity primeryEntity, Entity secenderyEntity) {
        if (prdCond.getSingularity().equals("single")) {
            Entity entity;
            if (prdCond.getEntity().equals(primeryEntity.getName())) {
                entity = primeryEntity;
            } else {
                entity = secenderyEntity;
            }
            return new SimpleCondition(
                    entity,
                    new CondExpression(prdCond.getProperty(), envVars, entities),
                    CondOp.fromString(prdCond.getOperator()),
                    new CondExpression(prdCond.getValue(), envVars, entities));
        } else if (prdCond.getSingularity().equals("multiple")) {
            List<ICond> conditions = new ArrayList<ICond>();
            for (PRDCondition condition : prdCond.getPRDCondition()) {
                conditions.add(getCondList(condition, primeryEntity, secenderyEntity));
            }
            return new ComplexCondition(conditions, primeryEntity, prdCond.getLogical());
        } else {
            throw new RuntimeException("Unknown condition type" + prdCond.getSingularity());
        }
    }

    private List<Action> getThenOrElseActions(List<PRDAction> thenOrrElseActions, Entity entity) {
        List<Action> result = null;
        result = new ArrayList<Action>();
        for (PRDAction thenOrrElseAction : thenOrrElseActions) {
            result.add(createAction(thenOrrElseAction));
        }
        return result;
    }

}
