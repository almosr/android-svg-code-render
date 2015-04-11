package android_svg_code_render;

import android.graphics.Canvas;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Main class
 *
 * @author Almos Rajnai
 */
public class Main {
    public static final String CANVAS_PARAMETER_NAME = "canvas";

    private static final String FILE_TEMPLATE =
            "package %s;\n\n" +                     //Package name
                    "%s\n" +                        //Imports
                    "public class %s {\n" +         //Class name
                    "\n%s\n" +                      //Dimension constants
                    "    public static void render(Canvas " + CANVAS_PARAMETER_NAME + ", int width, int height) {\n" +
                    "        canvas.save();\n" +
                    "        canvas.scale(width / WIDTH, height / HEIGHT);\n" +
                    "%s" +                          //Generated source
                    "        canvas.restore();\n" +
                    "    }\n}\n";

    private static String sInputFileName;
    private static String sSimpleInputFileName;
    private static String sOutFileName;
    private static String sPackageName;
    private static String sClassName;
    private static String sTemplate;

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
    }

    private static void extractParameters(String[] args) {
        //TODO: proper parsing of the command line parameters

        if (args.length < 1 || args.length > 5) {
            printHelp();
            error("Wrong arguments");
        }

        sInputFileName = args[0];
        sSimpleInputFileName = new File(sInputFileName).getName();

        sPackageName = "vector_render";
        if (args.length > 1) {
            sPackageName = args[1];
        }

        sClassName = "VectorRender_" + sInputFileName.split(".svg")[0];
        if (args.length > 2) {
            sClassName = args[2];
        }

        sOutFileName = sClassName + ".java";
        if (args.length > 3) {
            sOutFileName = args[3];
        }

        sTemplate = FILE_TEMPLATE;
        if (args.length > 4) {
            try {
                byte[] encoded = Files.readAllBytes(Paths.get(args[4]));
                sTemplate = new String(encoded);
            } catch (IOException e) {
                throw new RuntimeException("Error while reading template file", e);
            }
        }
    }

    private static void render(String inputFileName) throws IOException, SVGParseException {
        FileInputStream is = new FileInputStream(inputFileName);

        SVG svg = SVG.getFromInputStream(is);

        OutputBuilder.sWidth = svg.getDocumentWidth();
        OutputBuilder.sHeight = svg.getDocumentHeight();

        //Main canvas object is created with the static instance name from the method parameters
        svg.renderToCanvas(new Canvas(CANVAS_PARAMETER_NAME, 1, 1, true));

        is.close();
    }

    private static void saveOutput(String outFileName, String result) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(outFileName);
        output.print(result);
        output.close();
    }

    private static void printHelp() {
        System.out.println(String.format("android-svg-code-render v%s (%s)", Version.FULL, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Version.BUILD_TIME)));
        System.out.println("Usage: android-svg-code-render inputfile.svg <package name> <class name> <outputfile.java> <template file>\n");
    }

    public static void error(String msg, Object... params) {
        error(null, msg, params);
    }

    public static void error(Exception e, String msg, Object... params) {
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
