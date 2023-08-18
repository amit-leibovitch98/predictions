package simulation.world.detail.rule.action.condition;

public enum ConditionLogicOp {
    AND("and"),
    OR("or");
    public final String symbol;

    ConditionLogicOp(String symbol) {
        this.symbol = symbol;
    }

    public static ConditionLogicOp fromString(String string) {
        switch(string) {
            case "and":
                return AND;
            case "or":
                return OR;
            default:
                throw new IllegalArgumentException("Unknown expression type: " + string);
        }
    }
}
