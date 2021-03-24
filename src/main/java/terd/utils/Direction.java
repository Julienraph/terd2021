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

    public static Direction getByValue(int value) {
        for (Direction d : values()) {
            if (d.getValue() == value) {
                return d;
            }
        }
        return null;
    }
}
