package simulation.utils;

public enum Type {
    FLOAT,
    BOOLEAN,
    STRING;

    public static Type fromString(String type) {
        if (type.equals("float")) {
            return FLOAT;
        } else if (type.equals("boolean")) {
            return BOOLEAN;
        } else if (type.equals("string")) {
            return STRING;
        } else {
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
