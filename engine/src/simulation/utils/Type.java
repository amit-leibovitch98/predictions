package simulation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Type {
    DECIMAL,
    FLOAT,
    BOOLEAN,
    STRING;

    public static Type fromString(String type) {
        switch (type) {
            case "decimal":
                return DECIMAL;
            case "float":
                return FLOAT;
            case "boolean":
                return BOOLEAN;
            case "string":
                return STRING;
            default:
                return null;
        }
    }

    public static boolean isBoolean(String simpleExpressionVale) {
        return simpleExpressionVale.equals("true") || simpleExpressionVale.equals("false");
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
