package simulation.world.detail.rule.action;

public enum ActionType {
    INCREASE,
    DECREASE,
    CALCULATION,
    CONDITION,
    SET,
    KILL;

    /*
    REPLACE,
    PROXIMITY;
     */

    public static ActionType fromString(String string) {
        if (string.equalsIgnoreCase("increase")) {
            return INCREASE;
        } else if (string.equalsIgnoreCase("decrease")) {
            return DECREASE;
        } else if (string.equalsIgnoreCase("calculation")) {
            return CALCULATION;
        } else if (string.equalsIgnoreCase("condition")) {
            return CONDITION;
        } else if (string.equalsIgnoreCase("set")) {
            return SET;
        } else if (string.equalsIgnoreCase("kill")) {
            return KILL;
        } else {
            throw new IllegalArgumentException("ActionType " + string + " not found");
        }
    }
}
