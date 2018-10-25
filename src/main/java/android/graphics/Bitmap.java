package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Bitmap class
 *
 * @author Almos Rajnai
 */
public class Bitmap extends AndroidClass {
    private final int mWidth;
    private final int mHeight;

    public Bitmap(int width, int height) {
        mWidth = width;
        mHeight = height;

        OutputBuilder.addImport(Bitmap.class);
        mInstanceName = generateInstanceName(Bitmap.class);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
