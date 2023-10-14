package simulation.utils.expression;

import simulation.utils.Type;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityInstance;
import simulation.world.detail.entity.EntityProperty;
import simulation.world.detail.environmentvariables.EnvironmentVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CondExpression {
    private final ExpressionType expressionType;
    private final List<String> args;
    private static List<EnvironmentVariable> envVars;
    private static List<Entity> entities;
    private String simpleExpressionVale;

    public CondExpression(String fullExpression, List<EnvironmentVariable> envVars, List<Entity> entities) {
        this.envVars = envVars;
        this.entities = entities;
        List<String> tempArgs = null;
        ExpressionType tempExpressionType = null;
        for (Entity entity : entities) {
            for (EntityProperty prop : entity.getProperties()) {
                if (prop.getName().equals(fullExpression)) {
                    this.expressionType = ExpressionType.PROPERTY;
                    this.args = Arrays.asList(fullExpression);
                    return;
                }
            }
        }
        try {
            List<String> expressionAsList = splitExpression(fullExpression);
            tempExpressionType = ExpressionType.fromString(expressionAsList.get(0));
            tempArgs = expressionAsList.subList(1, expressionAsList.size());
        } catch (IllegalArgumentException e) {
            this.simpleExpressionVale = fullExpression;
            tempExpressionType = ExpressionType.SIMPLE;
            tempArgs = null;
        } finally {
            this.expressionType = tempExpressionType;
            this.args = tempArgs;
        }
    }

    public Object resolveExpression(EntityInstance entityInstance) {
        if (expressionType == ExpressionType.SIMPLE) {
            if (Type.isInteger(simpleExpressionVale)) {
                return Integer.parseInt(simpleExpressionVale) * 1.0f;
            } else if (Type.isFloat(simpleExpressionVale)) {
                return Float.parseFloat(simpleExpressionVale);
            } else if (Type.isBoolean(simpleExpressionVale)) {
                if (simpleExpressionVale.equals("true")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return simpleExpressionVale;
            }
        } else if (expressionType == ExpressionType.PROPERTY) {
            return entityInstance.getPropertyVal(args.get(0));
        } else {
            switch (expressionType) {
                case ENVIROMENT:
                    return environment(args.get(0));
                case RANDOM:
                    return random(args.get(0));
                case EVALUATE:
                    return evaluate(entityInstance, entityInstance, args.get(0), args.get(1));
                case PERCENT:
                    return percent(entityInstance, entityInstance,  args.get(0), args.get(1));
                case TICKS:
                    return ticks(entityInstance, entityInstance, args.get(0), args.get(1));
                default:
                    throw new IllegalArgumentException("Unknown expression type: " + expressionType);
            }
        }
    }

    public Object resolveExpression(EntityInstance primeryInstance, EntityInstance secendaryInstance) {
        if (expressionType == ExpressionType.SIMPLE) {
            if (Type.isInteger(simpleExpressionVale)) {
                return Integer.parseInt(simpleExpressionVale) * 1.0f;
            } else if (Type.isFloat(simpleExpressionVale)) {
                return Float.parseFloat(simpleExpressionVale);
            } else if (Type.isBoolean(simpleExpressionVale)) {
                if (simpleExpressionVale.equals("true")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return simpleExpressionVale;
            }
        } else if (expressionType == ExpressionType.PROPERTY) {
            return primeryInstance.getPropertyVal(args.get(0));
        } else {
            switch (expressionType) {
                case ENVIROMENT:
                    return environment(args.get(0));
                case RANDOM:
                    return random(args.get(0));
                case EVALUATE:
                    return evaluate(primeryInstance, secendaryInstance, args.get(0), args.get(1));
                case PERCENT:
                    return percent(primeryInstance, secendaryInstance, args.get(0), args.get(1));
                case TICKS:
                    return ticks(primeryInstance, secendaryInstance, args.get(0), args.get(1));
                default:
                    throw new IllegalArgumentException("Unknown expression type: " + expressionType);
            }
        }
    }

    private Integer ticks(EntityInstance entityInstance, EntityInstance secendaryInstance, String entityName, String propertyName) {
        if (entityInstance.getName().equals(entityName)) {
            Integer res = entityInstance.getPropertyLastUpdatedTick(propertyName);
            if (res != null) {
                return res;
            } else {
                throw new IllegalArgumentException("Unknown property: " + propertyName);
            }
        } else if (secendaryInstance.getName().equals((entityName))) {
            Integer res = secendaryInstance.getPropertyLastUpdatedTick(propertyName);
            if (res != null) {
                return res;
            } else {
                throw new IllegalArgumentException("Unknown property: " + propertyName);
            }
        } else {
            throw new IllegalArgumentException("Wrong entity: " + entityName);
        }
    }

    private Object evaluate(EntityInstance entityInstance, EntityInstance secendaryInstance, String entityName, String propertyName) {
        if (entityInstance.getName().equals(entityName)) {
            Object res = entityInstance.getPropertyVal(propertyName);
            if (res != null) {
                return res;
            } else {
                throw new IllegalArgumentException("Unknown property: " + propertyName + "for entity: " + entityName);
            }
        } else if (secendaryInstance.getName().equals(entityName)){
            Object res = secendaryInstance.getPropertyVal(propertyName);
            if (res != null) {
                return res;
            } else {
                throw new IllegalArgumentException("Unknown property: " + propertyName + "for entity: " + entityName);
            }
        } else {
            throw new IllegalArgumentException("Wrong entity: " + entityName);
        }
    }

    private Object percent(EntityInstance entityInstance, EntityInstance secndaryInstance, String whole, String part) {
        CondExpression wholeExpression = new CondExpression(whole, envVars, entities);
        CondExpression partExpression = new CondExpression(part, envVars, entities);
        Object wholeObj = wholeExpression.resolveExpression(entityInstance, secndaryInstance);
        Object partObj = partExpression.resolveExpression(entityInstance, secndaryInstance);
        if (wholeObj instanceof Float && partObj instanceof Float) {
            return Math.round((Float) wholeObj *
                    (Float) partObj / 100);
        } else {
            throw new IllegalArgumentException("Whole and part must be of type numeric");
        }
    }

    private Object environment(String envName) {
        for (EnvironmentVariable var : envVars) {
            if (var.getName().equals(envName)) {
                return var.getValue();
            }
        }
        throw new IllegalArgumentException("Unknown environment variable: " + envName);
    }

    private List<String> splitExpression(String expression) {
        List<String> result = new ArrayList<>();

        int openingParenthesis = expression.indexOf("(");
        int closingParenthesis = expression.lastIndexOf(")");

        if (openingParenthesis != -1 && closingParenthesis != -1) {
            String functionName = expression.substring(0, openingParenthesis).trim();
            String argumentsString = expression.substring(openingParenthesis + 1, closingParenthesis);

            result.add(functionName);
            if (functionName.equals("percent")) {
                String[] argArray = argumentsString.split(",");
                for (String arg : argArray) {
                    result.add(arg.trim());
                }
            } else {
                String[] argArray = argumentsString.split("[,.]");
                for (String arg : argArray) {
                    result.add(arg.trim());
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }
        return result;
    }

    private Object random(String max) {
        Random random = new Random();
        return random.nextInt(Integer.parseInt(max) + 1);
    }

    public static void updateEnvVars(List<EnvironmentVariable> envVars) {
        CondExpression.envVars = envVars;
    }
}
