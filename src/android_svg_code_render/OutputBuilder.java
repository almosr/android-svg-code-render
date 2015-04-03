package android_svg_code_render;

import java.util.*;

/**
 * Output builder class for collecting and formatting generated Java file from method calls
 *
 * @author Almos Rajnai
 */
public class OutputBuilder {
    private static ArrayList<OutputItem> sOutput;
    private static SortedSet<String> sImports;
    private static HashSet<AndroidClass> sInstances;

    public static float sWidth;
    public static float sHeight;

    public static void init() {
        sOutput = new ArrayList<>();
        sImports = new TreeSet<>();
        sInstances = new HashSet<>();
    }

    public static String getResult(String fileName, String fileTemplate, String packageName, String className) {

        optimize();

        StringBuilder str = new StringBuilder(10000);

        str.append("/**\n");
        str.append(" * This file was created by converting SVG file into Java code\n");
        str.append(String.format(" * Original file name: %s\n", fileName));
        str.append(String.format(" * Conversion date: %s\n", new Date().toString()));
        str.append(" *\n");
        str.append(String.format(" * Converted by android-svg-code-render v%s\n", Version.FULL));
        str.append(" * https://github.com/racsdragon/android-svg-code-render\n");
        str.append(" **/\n\n");

        //In case it is not imported yet, although it is unlikely:
        //we need the Canvas class as a parameter for the render method
        addImport(android.graphics.Canvas.class);

        StringBuilder strImports = new StringBuilder();
        for (String include : sImports) {
            strImports.append(String.format("import %s;\n", include));
        }

        String strDimensions = String.format(
                "    public static final float WIDTH = %ff;\n" +
                        "    public static final float HEIGHT = %ff;\n", sWidth, sHeight);

        str.append(String.format(fileTemplate, packageName, strImports, className, strDimensions, mergeOutput()));

        return str.toString();
    }

    public static void append(AndroidClass instance, String text, Object... params) {
        sOutput.add(new OutputItem(instance, String.format("        " + text + "\n", params)));

        sInstances.add(instance);
    }

    public static void appendMethodCall(AndroidClass instance, String methodName) {
        appendMethodCall(instance, methodName, null);
    }

    public static void appendMethodCall(AndroidClass instance, String methodName, String parameters, Object... objects) {
        if (parameters == null) {
            parameters = "";
        }
        append(instance, String.format("%s.%s(%s);", instance.getInstanceName(null), methodName, String.format(parameters, objects)));
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
        return String.format("new float[] %s", Arrays.toString(array).replace("[", "{").replace("]", "f}").replace(",", "f,"));
    }

    public static String createArrayParameter(int[] array, boolean hexFormat) {
        String numFormat = hexFormat ? "0x%08x" : "%d";
        StringBuilder numlist = new StringBuilder();
        numlist.append("new int[] {");
        for (int i = 0; i < array.length; i++) {
            numlist.append(String.format(numFormat, array[i]));
            if (i != array.length - 1) {
                numlist.append(", ");
            }
        }
        numlist.append("}");
        return numlist.toString();
    }

    public static void addImport(Class clazz) {
        sImports.add(clazz.getName().replace("$", "."));
    }

    private static void optimize() {

        //Remove instances which will not be used as a reference by the drawing operations (canvas)
        boolean wasChange;
        do {
            wasChange = false;

            for (AndroidClass instance : sInstances) {
                wasChange |= instance.removeUnusedDependencies();
            }

        } while (wasChange);

        //Remove Canvas.save and Canvas.Restore in a row, these are useless without doing anything in between the two
        //TODO: improve canvas.save/restore optimization, take account those calls only which are changing the saved properties of the canvas
        Stack<Integer> saveStack = new Stack<>();
        for (int i = 0; i < sOutput.size(); ) {
            String item = sOutput.get(i).getOutput().trim();
            if (item.equals("canvas.save();")) {
                saveStack.push(i);
                i++;
            } else if (item.equals("canvas.restore();") && !saveStack.empty()) {
                int index = saveStack.pop();

                if (index != -1) {
                    //Remove current restore call
                    sOutput.remove(i);

                    //Remove matching save call
                    sOutput.remove(index);

                    //Step one item back, we have removed two items, so this will be simple incrementing by one item at the end
                    i--;
                } else {
                    //This is a legit save-restore pair: skip removing
                    i++;
                }
            } else if (item.startsWith("canvas.")) {
                if (!saveStack.empty()) {
                    //Current item is interaction with the canvas: we cannot remove any previous save,
                    //let's mark the save on the top by replacing its index with -1
                    saveStack.pop();
                    saveStack.push(-1);
                }

                i++;
            } else {
                //Nothing special, go on
                i++;
            }
        }
    }

    private static String mergeOutput() {
        StringBuilder output = new StringBuilder(10000);

        for (OutputItem outputItem : sOutput) {
            if (outputItem.getInstance().isUsed()) {
                output.append(outputItem.getOutput());
            }
        }

        return output.toString();
    }

    private static class OutputItem {
        private final AndroidClass mInstance;
        private final String mOutput;

        public OutputItem(AndroidClass instance, String output) {
            mInstance = instance;
            mOutput = output;
        }

        public AndroidClass getInstance() {
            return mInstance;
        }

        public String getOutput() {
            return mOutput;
        }
    }
}
