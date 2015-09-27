package android_svg_code_render;

import java.util.*;

/**
 * Output builder class for collecting and formatting generated Java file from method calls
 *
 * @author Almos Rajnai
 */
public class OutputBuilder {
    private static final int METHOD_SIZE_THRESHOLD = 3000;

    private static ArrayList<OutputItem> sStaticItems;
    private static ArrayList<OutputItem> sInitOutput;
    private static ArrayList<OutputItem> sOutput;
    private static SortedSet<String> sImports;
    private static HashSet<AndroidClass> sInstances;
    private static int sSubMethodCounter;

    public static float sWidth;
    public static float sHeight;

    public static void init() {
        sStaticItems = new ArrayList<>();
        sInitOutput = new ArrayList<>();
        sOutput = new ArrayList<>();
        sImports = new TreeSet<>();
        sInstances = new HashSet<>();
        sSubMethodCounter = 0;
    }

    public static String getResult(String fileName, String fileTemplate, String packageName, String className) {

        optimize();

        splitMethod();

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

        str.append(String.format(fileTemplate, packageName, strImports, className, mergeConstants(), mergeFields(), mergeInits(), mergeOutput()));

        return str.toString();
    }

    public static void appendInit(AndroidClass instance, AndroidClass instanceCreated, AndroidClass[] dependencies, String text, Object... params) {
        appendInternal(sInitOutput, instance, instanceCreated, dependencies, text, params);
    }

    public static void append(AndroidClass instance, AndroidClass instanceCreated, AndroidClass[] dependencies, String text, Object... params) {
        appendInternal(sOutput, instance, instanceCreated, dependencies, text, params);
    }

    public static void appendMethodCall(AndroidClass instance, AndroidClass[] dependencies, String methodName) {
        appendMethodCall(instance, dependencies, methodName, null);
    }

    public static void appendMethodCall(AndroidClass instance, AndroidClass[] dependencies, String methodName, String parameters, Object... objects) {
        if (parameters == null) {
            parameters = "";
        }
        append(instance, null, dependencies, String.format("%s.%s(%s);", instance.getInstanceName(null), methodName, String.format(parameters, objects)));
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

    public static void appendConstant(AndroidClass constant, String line) {
        sStaticItems.add(new OutputItem(constant, constant, null, line));
    }

    public static void addImport(Class clazz) {
        sImports.add(clazz.getName().replace("$", "."));
    }

    public static AndroidClass[] dependencyList(AndroidClass... dependencies) {
        return dependencies;
    }

    private static void appendInternal(ArrayList<OutputItem> outputList, AndroidClass instance, AndroidClass instanceCreated, AndroidClass[] dependencies, String text, Object[] params) {
        outputList.add(new OutputItem(instance, instanceCreated, dependencies, text != null ? String.format("        " + text + "\n", params) : null));

        sInstances.add(instance);
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

    private static void splitMethod() {
        //Check if the output buffer contains more items than the method size threshold,
        //if not then there is nothing to adjust
        if (sOutput.size() < METHOD_SIZE_THRESHOLD) {
            return;
        }

        //Split method into smaller chunks which will fit into 64k compiled bytecode size
        ArrayList<OutputItem> items = new ArrayList<>(sOutput.subList(0, METHOD_SIZE_THRESHOLD));
        for (int i = METHOD_SIZE_THRESHOLD; i < sOutput.size(); i += METHOD_SIZE_THRESHOLD) {

            //Insert daisy-chain method calling and new method definition before the next block of code
            String line = String.format("        render_%d(canvas);\n    }\n\n    private void render_%1$d(Canvas canvas) {\n", sSubMethodCounter++);
            items.add(new OutputItem(null, null, null, line));

            //Copy over the code block (or remaining part of it)
            items.addAll(sOutput.subList(i, Math.min(i + METHOD_SIZE_THRESHOLD, sOutput.size())));
        }

        //New list will be replacing the previous output list
        sOutput = items;
    }

    private static String mergeFields() {
        StringBuilder output = new StringBuilder();

        for (OutputItem outputItem : sInitOutput) {
            if (outputItem.getInstance() != null && outputItem.getInstance().isUsed()) {
                output.append(String.format("    private %s %s;\n", outputItem.getInstance().getClass().getSimpleName(), outputItem.getInstance().getInstanceName(null)));
            }
        }

        return output.toString();
    }

    private static String mergeInits() {
        StringBuilder output = new StringBuilder();

        for (OutputItem outputItem : sInitOutput) {
            String line = outputItem.getOutput();
            if (outputItem.getInstance() != null && outputItem.getInstance().isUsed() && line != null) {
                output.append(line);
            }
        }

        return output.toString();
    }

    private static String mergeOutput() {
        StringBuilder output = new StringBuilder(10000);

        for (OutputItem outputItem : sOutput) {
            if (outputItem.getInstance() == null || outputItem.getInstance().isUsed()) {
                output.append(outputItem.getOutput());
            }
        }

        return output.toString();
    }

    private static String mergeConstants() {
        StringBuilder output = new StringBuilder();

        output.append(String.format(
                "    public static final float WIDTH = %ff;\n" +
                        "    public static final float HEIGHT = %ff;\n", sWidth, sHeight));

        for (OutputItem outputItem : sStaticItems) {
            if (outputItem.getInstance() != null && outputItem.getInstance().isUsed()) {
                output.append("    ");
                output.append(outputItem.getOutput());
                output.append("\n");
            }
        }

        output.append("\n");

        return output.toString();
    }

    private static class OutputItem {
        private final AndroidClass mInstance;
        private AndroidClass[] mDependencies;
        private final String mOutput;
        private final AndroidClass mInstanceCreated;

        public OutputItem(AndroidClass instance, AndroidClass instanceCreated, AndroidClass[] dependencies, String output) {
            mInstance = instance;
            mInstanceCreated = instanceCreated;
            mDependencies = dependencies;
            mOutput = output;
        }

        public AndroidClass getInstance() {
            return mInstance;
        }

        public String getOutput() {
            return mOutput;
        }

        public AndroidClass getInstanceCreated() {
            return mInstanceCreated;
        }

        public AndroidClass[] getDependencies() {
            return mDependencies;
        }
    }
}
