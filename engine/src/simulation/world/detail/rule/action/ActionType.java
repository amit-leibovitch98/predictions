package simulation.world.detail.rule.action;

public enum ActionType {
    INCREASE,
    DECREASE,
    CALCULATION,
    CONDITION,
    SET,
    KILL,
    REPLACE,
    PROXIMITY;


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
        } else if (string.equalsIgnoreCase("replace")) {
            return REPLACE;
        } else if (string.equalsIgnoreCase("proximity")) {
            return PROXIMITY;
        } else {
            throw new IllegalArgumentException("ActionType " + string + " not found");
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case INCREASE:
                return "increase";
            case DECREASE:
                return "decrease";
            case CALCULATION:
                return "calculation";
            case CONDITION:
                return "condition";
            case SET:
                return "set";
            case KILL:
                return "kill";
            case REPLACE:
                return "replace";
            case PROXIMITY:
                return "proximity";
            default:
                throw new IllegalArgumentException("ActionType " + this + " not found");
        }
    }
}
