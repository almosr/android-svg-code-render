package android_svg_code_render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by racs on 2015.03.17..
 */
public class OutputBuilder {
    private static StringBuilder output;
    private static SortedSet<String> imports = new TreeSet<>();

    public static void init() {
        output = new StringBuilder();
    }

    public static String getResult() {
        StringBuilder str = new StringBuilder();

        for (String include : imports) {
            str.append(String.format("import %s;\n", include));
        }

        str.append("\n");

        str.append(output);

        return str.toString();
    }

    public static void append(String text, Object... params) {
        output.append(String.format(text + "\n", params));
    }

    public static void appendMethodCall(AndroidClass instance, String methodName) {
        appendMethodCall(instance, methodName, null);
    }

    public static void appendMethodCall(AndroidClass instance, String methodName, String parameters, Object... objects) {
        if (parameters == null) {
            parameters = "";
        }
        append(String.format("%s.%s(%s);", instance.getInstanceName(), methodName, String.format(parameters, objects)));
    }

    public static String splitFlags(int flags, String prefix, int[] values, String[] names) {
        if (flags == 0) {
            return "0";
        }

        ArrayList<String> flagList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            if ((flags & values[i]) != 0) {
                flagList.add(prefix + names[i]);
            }
        }

        String[] outputList = new String[flagList.size()];
        flagList.toArray(outputList);

        return Arrays.toString(outputList).replace(",", " |").replace("[", "").replace("]", "");
    }

    public static String createArrayParameter(float[] array) {
        return String.format("new float[] %s", Arrays.toString(array).replace("[", "{").replace("]", "}"));
    }

    public static String createArrayParameter(int[] array) {
        return String.format("new int[] %s", Arrays.toString(array).replace("[", "{").replace("]", "}"));
    }

    public static void addImport(Class clazz) {
        imports.add(clazz.getName());
    }
}
