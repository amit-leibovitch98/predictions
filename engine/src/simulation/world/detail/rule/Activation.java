package simulation.world.detail.rule;

public class Activation {
    private final Integer ticks;
    private final Double probability;

    public Activation(Integer ticks, Double probability) {
        if(ticks == null) ticks = 1;
        this.ticks = ticks;
        if(probability == null) ticks = 1;
        this.probability = probability;
    }
}
