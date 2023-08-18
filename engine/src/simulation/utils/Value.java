package simulation.utils;

public class Value {
    private final boolean randomInitialize;
    private final Object init;
    private Object value;

    public Value(Object init, boolean randomInitialize, Range range) {
        this.randomInitialize = randomInitialize;
        this.init = init;
        if (randomInitialize) {
            this.value = range.getRandomValue();
        } else {
            this.value = init;

        }
    }

    public Object getCurrValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

}
