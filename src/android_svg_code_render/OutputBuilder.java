package android_svg_code_render;

/**
 * Created by racs on 2015.03.17..
 */
public class OutputBuilder {
    private static StringBuilder output;

    public static void init() {
        output = new StringBuilder();
    }

    public static String getResult() {
        return output.toString();
    }

    public static void append(String text) {
        output.append(text + "\n");
    }
}
