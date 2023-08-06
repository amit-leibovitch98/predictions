package simulation.utils.expression;

import simulation.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Expression {
    private String expression;
    private ExpressionType expressionType;
    private List<String> args;
    private World world;

    public Expression(String fullExpression) {
        this.expression = expression;
        this.expressionType = ExpressionType.fromString(expression);
        this.args = new ArrayList<String>();
    }

    public void unfoldExpression() {
        //FIXME: first val should be the expression type
        String[] splitExpression = expression.split(" ");
        args = Arrays.asList(splitExpression);
    }
    public String resolveExpression() {
        switch(expressionType) {
            case ENVIROMENT:
                break;
                return environment(world, args.get(0));
            case RANDOM:
                return random(world, args.get(0));
                break;
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

    private String environment(String envName) {
        //TODO: implement
        return "";
    }

    private String random(World world, String max) {
        return String.valueOf((int)(Math.random() * Integer.parseInt(max)));
    }
}
