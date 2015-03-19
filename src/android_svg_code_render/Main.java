package android_svg_code_render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by racs on 2015.03.17..
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            printHelp();
            return;
        }

        OutputBuilder.init();

        String inputFileName = args[0];
        String outFileName = args[1];

        try {
            render(inputFileName);
        } catch (IOException | SVGParseException | RuntimeException e) {
            error(e, "Error while rendering SVG file: %s", inputFileName);
        }

        try {
            saveOutput(outFileName, OutputBuilder.getResult());
        } catch (FileNotFoundException e) {
            error(e, "Error while saving result");
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
            String.format(": %s - %s", e.getClass().getName(), e.getMessage());
        }
        System.out.format("%s%s\n", String.format(msg, params), error);

        throw new RuntimeException(e);
    }
}
