package simulation.world.detail.rule;

public class Activation {
    private final Integer ticks;
    private final Double probability;

    public Activation(Integer ticks, Double probability) {
        if (ticks == null) {
            ticks = 1;
        }
        this.ticks = ticks;
        if (probability == null) {
            probability = 1.0;
        }
        this.probability = probability;
    }

    public Activation() {
        this.ticks = 1;
        this.probability = 1.0;
    }
    public double getProbability() {
        return probability;
    }
    @Override
    public String toString() {
        return "Activation: {" +
                "ticks=" + ticks +
                ", probability=" + probability +
                '}';
    }
}
