package android.graphics;

import android.content.res.AssetManager;
import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Typeface class
 *
 * @author Almos Rajnai
 */
public class Typeface extends AndroidClass {
    public static final Typeface DEFAULT = new Typeface("Typeface.DEFAULT");
    public static final Typeface SERIF = new Typeface("Typeface.SERIF");
    public static final Typeface SANS_SERIF = new Typeface("Typeface.SANS_SERIF");
    public static final Typeface MONOSPACE = new Typeface("Typeface.MONOSPACE");

    public static final int NORMAL = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public static final int BOLD_ITALIC = 3;
    private static int[] styleValues = {NORMAL, BOLD, ITALIC};
    private static String[] styleNames = {"NORMAL", "BOLD", "ITALIC"};

    public Typeface(String instanceName) {
        mInstanceName = instanceName;
    }

    public static Typeface createFromAsset(AssetManager assetManager, String s) {
        throw new RuntimeException("Dummy function");
    }

    public static Typeface create(Typeface family, int typefaceStyle) {
        Typeface newTypeface = new Typeface(generateInstanceName(Typeface.class));

        OutputBuilder.addImport(Typeface.class);

        String styleFlags = OutputBuilder.splitFlags(typefaceStyle, "Typeface.", styleValues, styleNames);
        OutputBuilder.appendInit(newTypeface, newTypeface, OutputBuilder.dependencyList(family), "%s = Typeface.create(%s, %s);", newTypeface.mInstanceName, family.getInstanceName(newTypeface), styleFlags);

        return newTypeface;

    }

    @Override
    public String getResetMethod() {
        return "";
    }
}
