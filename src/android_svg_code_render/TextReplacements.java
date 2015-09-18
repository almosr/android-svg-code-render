package android_svg_code_render;

import android.util.Log;

import java.util.ArrayList;

/**
 * Provider class for replacing texts in the output file by dynamic variables
 *
 * @author Almos Rajnai
 */
public class TextReplacements {
    private static final String TAG = TextReplacement.class.getName();

    private static ArrayList<TextReplacement> sTextReplacements = new ArrayList<>();

    public static void addTextReplacement(String text, String variable) {
        sTextReplacements.add(new TextReplacement(text, variable));
    }

    public static String findTextReplacement(String text) {
        for (TextReplacement textReplacement : sTextReplacements) {
            if (textReplacement.text.equals(text)) {
                textReplacement.used = true;
                return textReplacement.parameter;
            }
        }

        return String.format("\"%s\"", text);
    }

    public static void verifyReplacementTextUsage() {
        for (TextReplacement textReplacement : sTextReplacements) {
            if (!textReplacement.used) {
                Log.w(TAG, String.format("Text for replacement was not found: '%s'", textReplacement.text));
            }
        }
    }

    private static class TextReplacement {
        String text;
        String parameter;
        boolean used;

        public TextReplacement(String text, String parameter) {
            this.text = text;
            this.parameter = parameter;
        }
    }
}
