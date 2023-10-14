package simulation.utils;

import java.util.Random;

public class Range {
    private float from;
    private float to;

    public Range(Double from, Double to) {
        this.from = Float.parseFloat(from.toString());
        this.to = Float.parseFloat(to.toString());
    }

    public float getFrom() {
        return from;
    }

    public float getTo() {
        return to;
    }

    public void setFrom(int min) {
        this.from = min;
    }

    public void setTo(int max) {
        this.to = max;
    }

    public float getRandomValue(Type type) {
        Random random = new Random();

        if (type == Type.DECIMAL) {
            return random.nextInt((Math.round(to) - Math.round(from)) + 1) + from;
        } else if (type == Type.FLOAT) {
            return from + random.nextFloat() * (to - from);
        } else {
            throw new IllegalArgumentException("Type is not supported");
        }
    }

    public boolean isInRange(float value) {
        return value >= from && value <= to;
    }

    public int getRandomIntValue() {
        return (int) Math.floor(Math.random() * (to - from + 1) + from);
    }

    @Override
    public String toString() {
        return "{" + "from=" + from + ", to=" + to + "}";
    }

    public boolean isInRange(int value) {
        return value >= from && value <= to;
    }
}

