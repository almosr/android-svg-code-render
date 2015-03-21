package android_svg_code_render;

import java.util.*;

/**
 * Created by racs on 2015.03.17..
 */
public class OutputBuilder {
    public static final String CANVAS_PARAMETER_NAME = "canvas";

    private static StringBuilder output;
    private static SortedSet<String> imports = new TreeSet<>();

    public static void init() {
        output = new StringBuilder();
    }

    public static String getResult(String fileName, String packageName, String className, String methodName) {
        StringBuilder str = new StringBuilder();

        str.append(String.format("package %s;\n\n", packageName));

        str.append("/**\n");
        str.append(" * Converted SVG file into Java code\n");
        str.append(String.format(" * Original file name: %s\n", fileName));
        str.append(String.format(" * Conversion date: %s\n", new Date().toString()));
        str.append(" *\n");
        str.append(" * Converted by android-svg-code-render\n");
        str.append(" * https://github.com/racsdragon/android-svg-code-render\n");
        str.append(" **/\n\n");

        //In case it is not imported yet, although it is unlikely:
        //we need the Canvas class as a parameter for the render method
        addImport(android.graphics.Canvas.class);

        for (String include : imports) {
            str.append(String.format("import %s;\n", include));
        }

        str.append("\n");

        str.append(String.format("public class %s {\n", className));
        str.append(String.format("    public static void %s(Canvas %s, int width, int height) {\n", methodName, CANVAS_PARAMETER_NAME));
        str.append("        canvas.scale(width, height);\n");
        str.append(output);
        str.append("    }\n}\n");

        return str.toString();
    }

    public static void append(String text, Object... params) {
        output.append(String.format("        " + text + "\n", params));
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
        return String.format("new float[] %s", Arrays.toString(array).replace("[", "{").replace("]", "f}").replace(",","f,"));
    }

    public static String createArrayParameter(int[] array) {
        return String.format("new int[] %s", Arrays.toString(array).replace("[", "{").replace("]", "}"));
    }

    public static void addImport(Class clazz) {
        imports.add(clazz.getName().replace("$","."));
    }
}
