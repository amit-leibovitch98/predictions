package simulation.utils;

public class Range {
    private float from;
    private float to;

    public Range(Double from, Double to) {
        this.from = Float.parseFloat(from.toString());
        this.to = Float.parseFloat(to.toString());
    }

    public float getFrom() { return from; }
    public float getTo() { return to; }
    public void setFrom(int min) { this.from = min; }
    public void setTo(int max) { this.to = max; }

    public float getRandomValue() {
        return (float) (Math.random() * (to - from) + from);
    }

    public int getRandomIntValue() {
        return (int) Math.floor(Math.random() * (to - from + 1) + from);
    }

    @Override
    public String toString() {
        return "{" + "from=" + from + ", to=" + to + "}";
    }
}

