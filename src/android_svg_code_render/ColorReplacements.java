package android_svg_code_render;

import android.util.Log;

import java.util.ArrayList;

/**
 * Provider class for replacing colors in the output file by dynamic variables
 *
 * @author Almos Rajnai
 */
public class ColorReplacements {
    private static final String TAG = ColorReplacements.class.getName();

    private static ArrayList<ColorReplacement> sColorReplacements = new ArrayList<>();

    public static void addColorReplacement(int color, String variable) {
        sColorReplacements.add(new ColorReplacement(color, variable));
    }

    public static String findColorReplacement(int color) {
        for (ColorReplacement colorReplacement : sColorReplacements) {
            if (colorReplacement.color == color) {
                colorReplacement.used = true;
                return colorReplacement.parameter;
            }
        }

        return String.format("0x%08x", color);
    }

    public static void verifyReplacementColorUsage() {
        for (ColorReplacement colorReplacement : sColorReplacements) {
            if (!colorReplacement.used) {
                Log.w(TAG, String.format("Color to replace was not found: #%08x", colorReplacement.color));
            }
        }
    }

    private static class ColorReplacement {
        int color;
        String parameter;
        boolean used;

        public ColorReplacement(int color, String parameter) {
            this.color = color;
            this.parameter = parameter;
        }
    }
}
