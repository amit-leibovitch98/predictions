package simulation.world.detail.rule;

public class Activation {
    private final Integer ticks;
    private final Float probability;

    public Activation(Integer ticks, Double probability) {
        if (ticks == null) {
            ticks = 1;
        }
        this.ticks = ticks;
        if (probability == null) {
            probability = 1.0;
        }
        this.probability = Float.parseFloat(probability.toString());
    }

    public Activation() {
        this.ticks = 1;
        this.probability = 1f;
    }
    public Float getProbability() {
        return probability;
    }

    public Integer getTicks() {
        return ticks;
    }

    @Override
    public String toString() {
        return "Activation: {" +
                "ticks=" + ticks +
                ", probability=" + probability +
                '}';
    }
}
