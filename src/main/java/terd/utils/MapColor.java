package terd.utils;

public enum MapColor {

    TREE('T', Color.BRIGHT_GREEN),
    LAKE('L', Color.BRIGHT_CYAN),
    BASE('.', Color.GREY),
    GRASS(',', Color.GREEN),
    TREASURE('$', Color.BRIGHT_YELLOW),
    REST_AREA('H', Color.RED),
    TRADER('C', Color.BLUE),
    HOUSE('M', Color.WHITE),
    HILL('X', Color.GREY),
    WALL('#', Color.GREY),
    EMPTY(' ', Color.BLACK),
    PLAYER('@', Color.BRIGHT_MAGENTA),
    TEST('-', Color.BLACK);

    private char symbol;
    private Color color;

    MapColor(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    public static MapColor getMapColorForSymbol(char symbol) {
        for (MapColor mapColor : MapColor.values()) {
            if (mapColor.getSymbol() == symbol) {
                return mapColor;
            }
        }
        return null;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getColoredString() {
        return Color.toColoredString(color, String.valueOf(symbol));
    }
}
