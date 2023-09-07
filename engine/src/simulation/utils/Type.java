package simulation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    public String toString() {
        if (this == DECIMAL) {
            return "decimal";
        } else if (this == FLOAT) {
            return "float";
        } else if (this == BOOLEAN) {
            return "boolean";
        } else if (this == STRING) {
            return "string";
        } else {
            return null;
        }
    }

    public static boolean isInteger(String inputStr) {
        Pattern pattern = Pattern.compile("^[-+]?\\d+$");
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean isFloat(String inputStr) {
        Pattern pattern = Pattern.compile("^[-+]?\\d*\\.?\\d+(?:[eE][-+]?\\d+)?$");
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
}
