package android_svg_code_render;

import android.graphics.Canvas;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Main class
 *
 * @author Almos Rajnai
 */
public class Main {

    private static String sInputFileName;
    private static String sSimpleInputFileName;
    private static String sOutFileName;
    private static String sPackageName;
    private static String sClassName;
    private static String sMethodName;

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
            saveOutput(sOutFileName, OutputBuilder.getResult(sSimpleInputFileName, sPackageName, sClassName, sMethodName));
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

        sPackageName = "svgrenderpackage";
        if (args.length > 1) {
            sPackageName = args[1];
        }

        sClassName = "SvgRenderClass_" + sInputFileName.split(".svg")[0];
        if (args.length > 2) {
            sClassName = args[2];
        }

        sMethodName = "render";
        if (args.length > 3) {
            sMethodName = args[3];
        }

        sOutFileName = sClassName + ".java";
        if (args.length > 4) {
            sOutFileName = args[4];
        }
    }

    private static void render(String inputFileName) throws IOException, SVGParseException {
        FileInputStream is = new FileInputStream(inputFileName);

        SVG svg = SVG.getFromInputStream(is);

        //Preset scaling: document is enforced into a 1.0x1.0 pixel square,
        //will be scaled to the right height at rendering
        svg.setRenderDPI(1.0f);
        svg.setDocumentWidth(1.0f);
        svg.setDocumentHeight(1.0f);

        //Main canvas object is created with the static instance name from the method parameters
        svg.renderToCanvas(new Canvas(OutputBuilder.CANVAS_PARAMETER_NAME, 1, 1));

        is.close();
    }

    private static void saveOutput(String outFileName, String result) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(outFileName);
        output.print(result);
        output.close();
    }

    private static void printHelp() {
        System.out.println(String.format("android-svg-code-render v%s (%s)", Version.FULL, new SimpleDateFormat("dd/mm/yyyy HH:mm").format(Version.BUILD_TIME)));
        System.out.println("Usage: android-svg-code-render inputfile.svg <package name> <class name> <method name> <outputfile.java>\n");
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
