package android_svg_code_render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Various methods for processing config files
 *
 * @author Almos Rajnai
 */
public class ConfigFileUtils {
    public static void readTextReplacementConfig(String filePath) throws IOException {
        HashMap<String, String> texts = readHashMapFromFile(filePath);
        for (String key : texts.keySet()) {
            TextReplacements.addTextReplacement(key, texts.get(key));
        }
    }

    public static void readColorReplacementConfig(String filePath) throws IOException {
        HashMap<String, String> texts = readHashMapFromFile(filePath);
        for (String key : texts.keySet()) {
            ColorReplacements.addColorReplacement(parseColor(key.trim()), texts.get(key));
        }
    }

    private static HashMap<String, String> readHashMapFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        HashMap<String, String> output = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] items = line.split("=");
            output.put(items[0], items[1]);
        }

        return output;
    }

    private static int parseColor(String s) {
        //Try to parse into int using this format: "#xxxxxxxx"
        if (!s.startsWith("#") || s.length() != 9) {
            throw new RuntimeException("Wrong format for replacement color, expected \"#aarrggbb\", found: " + s);
        }
        try {
            return Long.valueOf(s.substring(1), 16).intValue();
        } catch (Exception e) {
            throw new RuntimeException("Wrong hexadecimal number for color: " + s);
        }
    }
}
