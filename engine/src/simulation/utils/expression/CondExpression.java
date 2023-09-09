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
        for(Entity entity : entities) {
            for(EntityProperty prop: entity.getProperties()) {
                if(prop.getName().equals(fullExpression)) {
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
        }
        finally {
            this.expressionType = tempExpressionType;
            this.args = tempArgs;
        }
    }

    public Object resolveExpression(EntityInstance entityInstance) {
        if(expressionType == ExpressionType.SIMPLE) {
            if(Type.isInteger(simpleExpressionVale)) {
                return Integer.parseInt(simpleExpressionVale);
            } else if(Type.isFloat(simpleExpressionVale)) {
                return Float.parseFloat(simpleExpressionVale);
            } else if(Type.isBoolean(simpleExpressionVale)) {
                if(simpleExpressionVale.equals("true")) {
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
                    return evaluate(entityInstance, args.get(0), args.get(1));
                case PRECENTAGE:
                    return precantage(args.get(0), args.get(1));
                case TICKS:
                    return ticks(entityInstance, args.get(0), args.get(1));
                default:
                    throw new IllegalArgumentException("Unknown expression type: " + expressionType);
            }
        }
    }

    private Integer ticks(EntityInstance entityInstance, String entityName, String propertyName) {
        if (entityInstance.getName().equals(entityName)) {
            Integer res = entityInstance.getPropertyLastUpdatedTick(propertyName);
            if (res != null) {
                return res;
            } else {
                throw new IllegalArgumentException("Unknown property: " + propertyName);
            }
        } else {
            throw new IllegalArgumentException("Wrong entity: " + entityName);
        }
    }

    private Object evaluate(EntityInstance entityInstance, String entityName, String propertyName) {
        if (entityInstance.getName().equals(entityName)) {
            Object res = entityInstance.getPropertyVal(propertyName);
            if (res != null) {
                return res;
            } else {
                throw new IllegalArgumentException("Unknown property: " + propertyName);
            }
        } else {
            throw new IllegalArgumentException("Wrong entity: " + entityName);
        }
    }

    private Object precantage(String whole, String part) {
        CondExpression wholeExpression = new CondExpression(whole, envVars, entities);
        CondExpression partExpression = new CondExpression(part, envVars, entities);
        if( wholeExpression.resolveExpression(null) instanceof Float && partExpression.resolveExpression(null) instanceof Float) {
            return Math.round((Float) wholeExpression.resolveExpression(null) *
                    (Float) partExpression.resolveExpression(null) / 100);
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
        int closingParenthesis = expression.indexOf(")");

        if (openingParenthesis != -1 && closingParenthesis != -1) {
            String functionName = expression.substring(0, openingParenthesis).trim();
            String argumentsString = expression.substring(openingParenthesis + 1, closingParenthesis);

            result.add(functionName);
            String[] argArray = argumentsString.split("[,.]");
            for (String arg : argArray) {
                result.add(arg.trim());
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
