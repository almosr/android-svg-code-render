package android_svg_code_render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by racs on 2015.03.17..
 */
public class Main {

    private static String sInputFileName;
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
            saveOutput(sOutFileName, OutputBuilder.getResult(sInputFileName, sPackageName, sClassName, sMethodName));
        } catch (FileNotFoundException e) {
            error(e, "Error while saving result");
        }
    }

    private static void extractParameters(String[] args) {
        //TODO: proper parsing of the command line parameters

        if (args.length < 2 || args.length > 5) {
            printHelp();
            error("Wrong arguments");
        }

        sInputFileName = args[0];
        sOutFileName = args[1];

        sPackageName = "svgrenderpackage";
        if (args.length > 2) {
            sPackageName = args[2];
        }

        sClassName = "SvgRenderClass_" + sInputFileName.split(".svg")[0];
        if (args.length > 3) {
            sClassName = args[3];
        }

        sMethodName = "render";
        if (args.length > 4) {
            sMethodName = args[4];
        }
    }

    private static void render(String inputFileName) throws IOException, SVGParseException {
        FileInputStream is = new FileInputStream(inputFileName);

        SVG svg = SVG.getFromInputStream(is);
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        svg.renderToCanvas(new Canvas(bitmap));

        is.close();
    }

    private static void saveOutput(String outFileName, String result) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(outFileName);
        output.print(result);
        output.close();
    }

    private static void printHelp() {
        System.out.println("Usage: android-svg-code-render <inputfile.svg> <outputfile.java>\n");
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
