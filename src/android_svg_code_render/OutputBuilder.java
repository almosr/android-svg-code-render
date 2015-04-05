package android_svg_code_render;

import java.util.*;

/**
 * Output builder class for collecting and formatting generated Java file from method calls
 *
 * @author Almos Rajnai
 */
public class OutputBuilder {
    private static final int METHOD_SIZE_THRESHOLD = 3000;

    private static ArrayList<OutputItem> sOutput;
    private static SortedSet<String> sImports;
    private static HashSet<AndroidClass> sInstances;
    private static int sSubMethodCounter;

    public static float sWidth;
    public static float sHeight;

    public static void init() {
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

        String strDimensions = String.format(
                "    public static final float WIDTH = %ff;\n" +
                        "    public static final float HEIGHT = %ff;\n", sWidth, sHeight);

        str.append(String.format(fileTemplate, packageName, strImports, className, strDimensions, mergeOutput()));

        return str.toString();
    }

    public static void append(AndroidClass instance, AndroidClass instanceCreated, AndroidClass[] dependencies, String text, Object... params) {
        sOutput.add(new OutputItem(instance, instanceCreated, dependencies, String.format("        " + text + "\n", params)));

        sInstances.add(instance);
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

    public static AndroidClass[] dependencyList(AndroidClass... dependencies) {
        return dependencies;
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

            //Get all dependencies of the remaining part of the code
            List<AndroidClass> dependencies = scanDependencies(i);

            //Sort dependencies according to their instance name
            Collections.sort(dependencies, new Comparator<AndroidClass>() {
                @Override
                public int compare(AndroidClass o1, AndroidClass o2) {
                    return o1.getInstanceName(null).compareTo(o2.getInstanceName(null));
                }
            });

            //Turn dependencies into method parameter and argument list
            String parameters = "";
            String arguments = "";
            for (int d = 0; d < dependencies.size(); d++) {
                AndroidClass instance = dependencies.get(d);
                parameters += instance.getInstanceName(null);
                arguments += String.format("%s %s", instance.getClass().getSimpleName(), instance.getInstanceName(null));
                if (d < dependencies.size() - 1) {
                    parameters += ", ";
                    arguments += ", ";
                }
            }

            //Insert daisy-chain method calling and new method definition before the next block of code
            String line = String.format("        render_%d(%s);\n    }\n\n    private static void render_%1$d(%s) {\n", sSubMethodCounter++, parameters, arguments);
            items.add(new OutputItem(null, null, null, line));

            //Copy over the code block (or remaining part of it)
            items.addAll(sOutput.subList(i, Math.min(i + METHOD_SIZE_THRESHOLD, sOutput.size())));
        }

        //New list will be replacing the previous output list
        sOutput = items;
    }

    private static List<AndroidClass> scanDependencies(int start) {
        HashSet<AndroidClass> createdInstances = new HashSet<>();
        HashSet<AndroidClass> depencencies = new HashSet<>();
        for (int j = start; j < sOutput.size(); j++) {
            //Gather created instances from output instances
            OutputItem item = sOutput.get(j);

            //Collect created instances into a hash set
            if (item.getInstanceCreated() != null) {
                createdInstances.add(item.getInstanceCreated());
            }

            if (item.getDependencies() != null) {
                //Check each dependency if it was created in the current block
                for (AndroidClass dependency : item.getDependencies()) {
                    if (!createdInstances.contains(dependency)) {
                        //Not created here, add as external depencency
                        depencencies.add(dependency);
                    }
                }
            }
        }

        return new ArrayList<>(depencencies);
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
