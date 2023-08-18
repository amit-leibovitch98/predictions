package simulation.utils;

public class Range {
    private double from;
    private double to;

    public Range(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public double getFrom() { return from; }
    public double getTo() { return to; }
    public void setFrom(int min) { this.from = min; }
    public void setTo(int max) { this.to = max; }

    public Object getRandomValue() {
        return Math.random() * (to - from) + from;
    }
}

