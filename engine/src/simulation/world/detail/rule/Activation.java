package simulation.world.detail.rule;

public class Activation {
    private final int ticks;
    private final double probability;

    public Activation(int ticks, double probability) {
        this.ticks = ticks;
        this.probability = probability;
    }
}
