package terd.utils;

public enum Direction {

    UP(1000),
    DOWN(100),
    LEFT(1),
    RIGHT(10);

    private int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
