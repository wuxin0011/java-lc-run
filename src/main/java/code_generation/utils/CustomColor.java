package code_generation.utils;


/**
 * A utility class for generating ANSI color codes for console output.
 * Provides methods for creating colored text and backgrounds with RGB values.
 * @author wuxin0011
 * @since 1.0
 */
public class CustomColor {

    /**
     * Maximum RGB value (255)
     */
    private static final int RGB_VALUE_MAX = 255;

    /**
     * ANSI code for background color
     */
    private static final int BACKGROUND_COLOR = 48;

    /**
     * ANSI code for font color
     */
    private static final int FONT_COLOR = 38;

    /**
     * ANSI code to clear color formatting
     */
    private static final String CLEAR_COLOR = "\u001B[0m";

    /**
     * Represents an RGB color value.
     */
    public static class MyColor {
        /**
         * Red component (0-255)
         */
        public int R;

        /**
         * Green component (0-255)
         */
        public int G;

        /**
         * Blue component (0-255)
         */
        public int L;

        /**
         * Creates a color with specific RGB values.
         * @param r Red value (0-255)
         * @param g Green value (0-255)
         * @param l Blue value (0-255)
         */
        public MyColor(int r, int g, int l) {
            this.R = r;
            this.G = g;
            this.L = l;
        }

        /**
         * Creates a color with random RGB values.
         */
        public MyColor() {
            this.R = randomColorValue();
            this.G = randomColorValue();
            this.L = randomColorValue();
        }
    }

    /**
     * Generates a random color value (0-255).
     * @return Random integer between 0 and 255
     */
    public static int randomColorValue() {
        return (int) Math.floor(Math.random() * RGB_VALUE_MAX);
    }

    /**
     * Creates colored text with random RGB values.
     * @param content The content to color
     * @return ANSI-formatted colored string
     */
    public static String fontColor(Object... content) {
        int R = randomColorValue();
        int G = randomColorValue();
        int L = randomColorValue();
        return fontColor(R, G, L, content);
    }

    /**
     * Creates colored text with specified MyColor.
     * @param myColor The color to use
     * @param content The content to color
     * @return ANSI-formatted colored string
     */
    public static String fontColor(MyColor myColor, Object... content) {
        return fontColor(myColor.R, myColor.G, myColor.L, content);
    }

    /**
     * Creates colored text with specific RGB values.
     * @param R Red value (0-255)
     * @param G Green value (0-255)
     * @param L Blue value (0-255)
     * @param content The content to color
     * @return ANSI-formatted colored string
     * @throws RuntimeException if RGB values exceed 255
     */
    public static String fontColor(int R, int G, int L, Object... content) {
        return colorTemplate(FONT_COLOR, R, G, L, content);
    }

    /**
     * Creates colored background with random RGB values.
     * @param content The content to color
     * @return ANSI-formatted string with colored background
     */
    public static String backgroundColor(Object... content) {
        int R = randomColorValue();
        int G = randomColorValue();
        int L = randomColorValue();
        return backgroundColor(R, G, L, content);
    }

    /**
     * Creates colored background with specified MyColor.
     * @param myColor The background color to use
     * @param content The content to color
     * @return ANSI-formatted string with colored background
     */
    public static String backgroundColor(MyColor myColor, Object... content) {
        return backgroundColor(myColor.R, myColor.G, myColor.L, content);
    }

    /**
     * Creates colored background with specific RGB values.
     * @param R Red value (0-255)
     * @param G Green value (0-255)
     * @param L Blue value (0-255)
     * @param content The content to color
     * @return ANSI-formatted string with colored background
     * @throws RuntimeException if RGB values exceed 255
     */
    public static String backgroundColor(int R, int G, int L, Object... content) {
        return colorTemplate(BACKGROUND_COLOR, R, G, L, content);
    }

    /**
     * Creates a colored string with specified color type and RGB values.
     * @param type Color type (BACKGROUND_COLOR or FONT_COLOR)
     * @param R Red value (0-255)
     * @param G Green value (0-255)
     * @param L Blue value (0-255)
     * @param content The content to color
     * @return ANSI-formatted colored string
     */
    public static String colorTemplate(int type, int R, int G, int L, Object... content) {
        valid(R, G, L);
        StringBuilder s = new StringBuilder();
        for (Object s1 : content) {
            s.append(s1);
        }
        return String.format("\u001B[%d;2;%d;%d;%dm%s\u001B[0m", type, R, G, L, s);
    }

    /**
     * Creates a string with both background and font colors.
     * @param R1 Background red value (0-255)
     * @param G1 Background green value (0-255)
     * @param L1 Background blue value (0-255)
     * @param R2 Font red value (0-255)
     * @param G2 Font green value (0-255)
     * @param L2 Font blue value (0-255)
     * @param content The content to color
     * @return ANSI-formatted string with colored background and text
     */
    public static String colorTemplate(int R1, int G1, int L1, int R2, int G2, int L2, String... content) {
        valid(R1, G1, L1, R2, G2, L2);
        StringBuilder s = new StringBuilder();
        for (String s1 : content) {
            s.append(s1);
        }
        return String.format("\u001B[%s;2;%d;%d;%dm\u001B[%s;2;%d;%d;%dm%s\u001B[0m",
                BACKGROUND_COLOR, R1, G1, L1, FONT_COLOR, R2, G2, L2, s.toString());
    }

    /**
     * Creates a string with both background and font colors using MyColor objects.
     * @param bc Background color
     * @param fc Font color
     * @param content The content to color
     * @return ANSI-formatted string with colored background and text
     */
    public static String colorTemplate(MyColor bc, MyColor fc, String... content) {
        return colorTemplate(bc.R, bc.G, bc.L, fc.R, fc.G, fc.L, content);
    }

    /**
     * Validates MyColor objects.
     * @param myColors Colors to validate
     * @throws RuntimeException if any RGB value exceeds 255
     */
    public static void valid(MyColor... myColors) {
        for (MyColor myColor : myColors) {
            valid(myColor.R, myColor.G, myColor.L);
        }
    }

    /**
     * Validates RGB values.
     * @param ints RGB values to validate
     * @throws RuntimeException if any value exceeds 255
     */
    public static void valid(int... ints) {
        for (int result : ints) {
            if (result > RGB_VALUE_MAX) {
                throw new RuntimeException("color value max is " + RGB_VALUE_MAX);
            }
        }
    }

    /**
     * Creates success-colored text (green).
     * @param content The content to color
     * @return ANSI-formatted green string
     */
    public static String success(Object... content) {
        return fontColor(89, 168, 105, content);
    }

    /**
     * Creates pink-colored text.
     * @param content The content to color
     * @return ANSI-formatted pink string
     */
    public static String pink(Object... content) {
        return fontColor(253, 54, 110, content);
    }

    /**
     * Creates error-colored text (red).
     * @param content The content to color
     * @return ANSI-formatted red string
     */
    public static String error(Object... content) {
        return fontColor(253, 54, 110, content);
    }

    /**
     * Prints success-colored text to System.out.
     * @param content The content to print
     */
    public static void printSuccess(Object... content) {
        System.out.println(success(content));
    }
}