package simulation.world.detail.rule.action.condition;

import simulation.utils.expression.ExpressionType;

public enum CondOp {
    EQUALS("="),
    NOT_EQUALS("!="),
    BIGGER_THAN("Bt"),
    LESSER_THAN("Lt");
    private final String symbol;

    CondOp(String symbol) {
        this.symbol = symbol;
    }

    public static CondOp fromString (String string) {
        switch(string) {
            case "=":
                return EQUALS;
            case "!=":
                return NOT_EQUALS;
            case "Bt":
                return BIGGER_THAN;
            case "Lt":
                return LESSER_THAN;
            default:
                throw new IllegalArgumentException("Unknown expression type: " + string);
        }
    }
}
