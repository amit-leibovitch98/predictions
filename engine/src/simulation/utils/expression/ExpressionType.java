package simulation.utils.expression;

public enum ExpressionType {
    SIMPLE("simple"),
    ENVIROMENT("environment"),
    RANDOM("random"),
    EVALUATE("evaluate"),
    PRECENTAGE("precentage"),
    TICKS("ticks");

    private final String name;

    ExpressionType(String name) {
        this.name = name;
    }

    public static ExpressionType fromString (String string) {
        switch(string) {
            case "simple":
                return SIMPLE;
            case "environment":
                return ENVIROMENT;
            case "random":
                return RANDOM;
            case "evaluate":
                return EVALUATE;
            case "precentage":
                return PRECENTAGE;
            case "ticks":
                return TICKS;
            default:
                throw new IllegalArgumentException("Unknown expression type: " + string);
        }
    }
}
