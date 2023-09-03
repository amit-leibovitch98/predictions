package simulation.utils;

public enum Type {
    FLOAT,
    BOOLEAN,
    STRING;

    public static Type fromString(String type) {
        switch (type) {
            case "float":
            case "decimal":
                return FLOAT;
            case "boolean":
                return BOOLEAN;
            case "string":
                return STRING;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
       if (this == FLOAT) {
            return "float";
        } else if (this == BOOLEAN) {
            return "boolean";
        } else if (this == STRING) {
            return "string";
        } else {
            return null;
        }
    }
}
