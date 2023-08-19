package simulation.utils.expression;

import simulation.utils.Type;
import simulation.world.detail.environmentvariables.EnvironmentVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CondExpression {
    private final ExpressionType expressionType;
    private final List<String> args;
    private static List<EnvironmentVariable> envVars;

    private Float simpleExpressionVale;

    public CondExpression(String fullExpression, List<EnvironmentVariable> envVars) {
        this.envVars = envVars;
        List<String> tempArgs = null;
        ExpressionType tempExpressionType = null;
        try {
            this.simpleExpressionVale = Float.parseFloat(fullExpression);
            tempExpressionType = ExpressionType.SIMPLE;
            tempArgs = null;
        } catch (NumberFormatException e) {
            List<String> expressionAsList = splitExpression(fullExpression);
            tempExpressionType = ExpressionType.fromString(expressionAsList.get(0));
            tempArgs = expressionAsList.subList(1, expressionAsList.size());
        }
        finally {
            this.expressionType = tempExpressionType;
            this.args = tempArgs;
        }
    }

    public Object resolveExpression() {
        if(expressionType == ExpressionType.SIMPLE) {
            return simpleExpressionVale;
        }
        switch(expressionType) {
            case ENVIROMENT:
                return environment(args.get(0));
            case RANDOM:
                return random(args.get(0));
            /*case EVALUATE:
                break;
            case PRECENTAGE:
                break;
            case TICKS:
                break;*/
            default:
                throw new IllegalArgumentException("Unknown expression type: " + expressionType);
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
            String[] argArray = argumentsString.split(",");
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
