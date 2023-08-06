package simulation.utils;

public class Range {
    private double from;
    private double to;

    public Range(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public double getMin() { return from; }
    public double getMax() { return to; }
    public void setMin(int min) { this.from = min; }
    public void setMax(int max) { this.to = max; }
}

