package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Bitmap class
 *
 * @author Almos Rajnai
 */
public class Bitmap extends AndroidClass {
    private int mWidth;
    private int mHeight;

    public Bitmap(int width, int height) {
        mWidth = width;
        mHeight = height;

        OutputBuilder.addImport(Bitmap.class);
        OutputBuilder.addImport(Config.class);
        mInstanceName = generateInstanceName(Bitmap.class);
    }

    public static Bitmap createBitmap(int width, int height, Config bitmapConfig) {
        Bitmap bitmap = new Bitmap(width, height);
        OutputBuilder.append(bitmap, bitmap, null, "Bitmap %s = Bitmap.createBitmap(%d, %d, Config.%s);", bitmap.getInstanceName(null), width, height, bitmapConfig.name());
        return bitmap;
    }

    public void recycle() {
        //No need to do anything for recycling bitmaps
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        //TODO: getPixels is needed for bitmap mask
        throw new RuntimeException("Bitmap.getPixels() method is not supported. (Used by bitmap masks.)");
    }

    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        //TODO: setPixels is needed for bitmap mask
        throw new RuntimeException("Bitmap.setPixels() method is not supported. (Used by bitmap masks.)");
    }

    public enum Config {ARGB_8888}
}
