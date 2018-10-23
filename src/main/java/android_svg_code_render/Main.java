package android_svg_code_render;

import android.graphics.Canvas;
import android.graphics.Typeface;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

/**
 * Main class
 *
 * @author Almos Rajnai
 */
public class Main {
    private static final String CANVAS_PARAMETER_NAME = "canvas";

    private static final int[] ANDROID_API_VERSIONS = {14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 28};

    private static final String FILE_TEMPLATE =
            "package %s;\n\n" +                       //Package name
                    "%s\n" +                          //Imports
                    "public class %s {\n" +           //Class name
                    "\n%s" +                          //Constants
                    "    private boolean inited;\n" + //Inited flag
                    "%s\n" +                          //Fields
                    "    private void init() {\n" +    //Init method header
                    "        if (inited) return;\n" + //Check for already executed init and leave if it was done
                    "        inited = true;\n" +      //Mark the init as executed
                    "%s" +                            //Init method body
                    "    }\n\n" +                     //Init method end
                    "    public void render(Canvas " + CANVAS_PARAMETER_NAME + ", int width, int height) {\n" + //Render method
                    "        init();\n" +             //Call init of fields
                    "        canvas.save();\n" +      //Save canvas state before rendering
                    "        canvas.scale(width / WIDTH, height / HEIGHT);\n" +  //Scale output to canvas size
                    "%s" +                            //Generated source
                    "        canvas.restore();\n" +   //Restore canvas state
                    "    }\n}\n";                     //Finished

    private static String sInputFileName;
    private static String sSimpleInputFileName;
    private static String sOutFileName;
    private static String sPackageName;
    private static String sClassName;
    private static String sTemplate;
    private static int sMinimumAndroidAPI = ANDROID_API_VERSIONS[0];
    private static HashSet<String> sAPIWarnings = new HashSet<>();

    public static void main(String[] args) {

        OutputBuilder.init();

        //Let's set the US locale to avoid problems with number format
        Locale.setDefault(Locale.US);

        extractParameters(args);

        try {
            render(sInputFileName);
        } catch (IOException | SVGParseException | RuntimeException e) {
            error(e, "Error while rendering SVG file: %s", sInputFileName);
        }

        try {
            saveOutput(sOutFileName, OutputBuilder.getResult(sSimpleInputFileName, sTemplate, sPackageName, sClassName));
        } catch (FileNotFoundException e) {
            error(e, "Error while saving result");
        }

        if (sAPIWarnings.size() > 0) {
            System.out.println(String.format("WARNING: hardware accelerated rendering is not available on specified minimum Android API v%d", sMinimumAndroidAPI));
            System.out.println("The following methods are used in rendered source code:");
            for (String method : sAPIWarnings) {
                System.out.println(method);
            }
        }
    }

    public static void checkAPIWarning(String method, int supportedAPI) {
        if (isLowerMinimumAPI(supportedAPI)) {
            sAPIWarnings.add(method);
        }
    }

    public static boolean isLowerMinimumAPI(int api) {
        return api > sMinimumAndroidAPI;
    }

    private static void extractParameters(String[] args) {
        if (args.length < 1 || args.length % 2 != 1) {
            printHelp();
            error("Wrong number of arguments");
        }

        //Print help for -h(elp)
        if (args[0].startsWith("-h")) {
            printHelp();
            System.exit(0);
        }

        sInputFileName = args[0];
        sSimpleInputFileName = new File(sInputFileName).getName();

        sPackageName = "vector_render";
        sClassName = "VectorRender_" + sInputFileName.split(".svg")[0];
        sTemplate = FILE_TEMPLATE;

        for (int i = 1; i < args.length; i += 2) {

            switch (args[i]) {
                case "-p":
                    sPackageName = args[i + 1];
                    break;

                case "-c":
                    sClassName = args[i + 1];
                    break;

                case "-o":
                    sOutFileName = args[i + 1];
                    break;

                case "-t":
                    try {
                        byte[] encoded = Files.readAllBytes(Paths.get(args[i + 1]));
                        sTemplate = new String(encoded);
                    } catch (IOException e) {
                        throw new RuntimeException("Error while reading template file", e);
                    }
                    break;

                case "-tfp":
                    Typeface.sTypefaceParameterName = args[i + 1];
                    break;

                case "-rt":
                    try {
                        ConfigFileUtils.readTextReplacementConfig(args[i + 1]);
                    } catch (Exception e) {
                        throw new RuntimeException("Error while parsing replacement text parameter", e);
                    }
                    break;

                case "-rc":
                    try {
                        ConfigFileUtils.readColorReplacementConfig(args[i + 1]);
                    } catch (Exception e) {
                        throw new RuntimeException("Error while parsing replacement color parameter", e);
                    }
                    break;

                case "-aos":
                    try {
                        int api = Integer.parseInt(args[i + 1]);
                        if (Arrays.stream(ANDROID_API_VERSIONS).noneMatch(x -> x == api)) {
                            throw new RuntimeException(String.format("Unknown Android API version: %d, supported versions: %s", api, Arrays.toString(ANDROID_API_VERSIONS)));
                        }
                        sMinimumAndroidAPI = api;
                    } catch (Exception e) {
                        throw new RuntimeException("Error while parsing minimum Android API version parameter", e);
                    }
                    break;

                default:
                    throw new RuntimeException("Unknown parameter: " + args[i]);
            }
        }

        //If the output file was not specified then it is derived from the class name
        if (sOutFileName == null) {
            sOutFileName = sClassName + ".java";
        }
    }

    private static void render(String inputFileName) throws IOException, SVGParseException {
        FileInputStream is = new FileInputStream(inputFileName);

        SVG svg = SVG.getFromInputStream(is);

        float width = svg.getDocumentWidth();
        float height = svg.getDocumentHeight();

        //Validate document dimensions
        if (width <= 0.0f || height <= 0.0f) {
            throw new RuntimeException(String.format(Locale.ENGLISH, "Illegal document dimensions: (%f:%f)\nMake sure document dimensions are set higher than 0 in input SVG file!", width, height));
        }

        OutputBuilder.setWidth(width);
        OutputBuilder.setHeight(height);

        //Main canvas object is created with the static instance name from the method parameters
        Canvas canvas = new Canvas(CANVAS_PARAMETER_NAME, 1, 1, true);
        svg.renderToCanvas(canvas);

        TextReplacements.verifyReplacementTextUsage();
        ColorReplacements.verifyReplacementColorUsage();

        is.close();
    }

    private static void saveOutput(String outFileName, String result) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(outFileName);
        output.print(result);
        output.close();
    }

    private static void printHelp() {
        System.out.println(String.format("android-svg-code-render v%s (%s)", Version.FULL, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Version.BUILD_TIME)));
        System.out.println("Usage: android-svg-code-render <inputfile.svg> [-p <package name>] [-c <class name>] [-o <outputfile.java>] [-t <template file>] [-tfp <typeface parameter name>] [-rt <text replacement file>] [-rc <color replacement file>]\n");
    }

    private static void error(String msg, Object... params) {
        error(null, msg, params);
    }

    private static void error(Exception e, String msg, Object... params) {
        String error = "";
        if (e != null) {
            error = String.format(": %s - %s", e.getClass().getName(), e.getMessage());
        }
        System.out.format("%s%s\n", String.format(msg, params), error);

        if (e != null) {
            throw new RuntimeException(e);
        } else {
            System.exit(1);
        }
    }
}
