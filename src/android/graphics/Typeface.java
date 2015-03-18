package android.graphics;

import android.content.res.AssetManager;

/**
 * Created by racs on 2015.03.17..
 */
public class Typeface {
    public static final Typeface DEFAULT = new Typeface();
    public static final Typeface SERIF = new Typeface();
    public static final Typeface SANS_SERIF = new Typeface();
    public static final Typeface MONOSPACE = new Typeface();

    public static final int NORMAL = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public static final int BOLD_ITALIC = 3;

    private Typeface() {
        throw new RuntimeException("Dummy function");
    }

    public static Typeface createFromAsset(AssetManager assetManager, String s) {
        throw new RuntimeException("Dummy function");
    }

    public static Typeface create(Typeface family, int typefaceStyle) {
        throw new RuntimeException("Dummy function");
    }
}
