package simulation.utils;

public class Value {
    private final boolean randomInitialize;
    private final String init;
    private String value;

    public Value(String init, boolean randomInitialize) {
        this.randomInitialize = randomInitialize;
        this.init = init;
        this.value = init;
    }

    public boolean isRandomInitialize() {
        return randomInitialize;
    }

    public String getInit() {
        return init;
    }

    public String getCurrValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
