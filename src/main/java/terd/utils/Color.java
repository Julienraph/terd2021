package terd.utils;

public enum Color {

    BLACK(30, 40),
    RED(31, 41),
    GREEN(32, 42),
    YELLOW(33, 43),
    BLUE(34, 44),
    MAGENTA(35, 45),
    CYAN(36, 46),
    WHITE(37, 47),
    GREY(90, 100),
    BRIGHT_RED(91, 101),
    BRIGHT_GREEN(92, 102),
    BRIGHT_YELLOW(93, 103),
    BRIGHT_BLUE(94, 104),
    BRIGHT_MAGENTA(95, 105),
    BRIGHT_CYAN(96, 106),
    BRIGHT_WHITE(97, 107);

    private int fg;
    private int bg;

    Color(int fg, int bg) {
        this.fg = fg;
        this.bg = bg;
    }

    public static String toColoredString(Color foreGround, Color background, String content) {

        String builder = "\033[" +
                foreGround.getFg() +
                ";" +
                background.getBg() +
                "m" +
                content +
                "\033[0m";
        return builder;
    }

    public static String toColoredString(Color foreGround, String content) {
        return toColoredString(foreGround, Color.BLACK, content);
    }

    public int getFg() {
        return fg;
    }

    public int getBg() {
        return bg;
    }
}
