package simulation.utils.expression;

public enum ExpressionType {
    SIMPLE("simple"),
    PROPERTY("property"),
    ENVIROMENT("environment"),
    RANDOM("random"),
    EVALUATE("evaluate"),
    PERCENT("precentage"),
    TICKS("ticks");

    private final String name;

    ExpressionType(String name) {
        this.name = name;
    }

    public static ExpressionType fromString(String string) {
        switch (string) {
            case "simple":
                return SIMPLE;
            case "property":
                return PROPERTY;
            case "environment":
                return ENVIROMENT;
            case "random":
                return RANDOM;
            case "evaluate":
                return EVALUATE;
            case "percent":
                return PERCENT;
            case "ticks":
                return TICKS;
            default:
                throw new IllegalArgumentException("Unknown expression type: " + string);
        }
    }
}
