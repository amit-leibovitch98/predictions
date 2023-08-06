package simulation.utils;

public enum Type {
    DECIMAL,
    FLOAT,
    BOOLEAN,
    STRING;

    public static Type fromString(String type) {
        if (type.equals("decimal")) {
            return DECIMAL;
        } else if (type.equals("float")) {
            return FLOAT;
        } else if (type.equals("boolean")) {
            return BOOLEAN;
        } else if (type.equals("string")) {
            return STRING;
        } else {
            return null;
        }
    }
}
