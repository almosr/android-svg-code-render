package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Bitmap class
 *
 * @author Almos Rajnai
 */
public class Bitmap extends AndroidClass {
    private int mWidth;
    private int mHeight;

    private Bitmap(int width, int height, Config config) {
        mWidth = width;
        mHeight = height;

        OutputBuilder.addImport(Bitmap.class);
        OutputBuilder.addImport(Config.class);
        mInstanceName = Initializer.generateInstanceName(Bitmap.class);
    }

    public static Bitmap createBitmap(int width, int height, Config bitmapConfig) {
        Bitmap bitmap = new Bitmap(width, height, bitmapConfig);
        OutputBuilder.append(bitmap, "Bitmap %s = Bitmap.createBitmap(%d, %d, Config.%s);", bitmap.getInstanceName(null), width, height, bitmapConfig.name());
        return bitmap;
    }

    public void recycle() {
        throw new RuntimeException("Dummy function");
    }

    public int getWidth() {
        //TODO: track getWidth
        return mWidth;
    }

    public int getHeight() {
        //TODO: track getHeight
        return mHeight;
    }

    public void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        throw new RuntimeException("Dummy function");
    }

    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        throw new RuntimeException("Dummy function");
    }

    public enum Config {ARGB_8888}
}
