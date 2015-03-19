package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

/**
 * Created by racs on 2015.03.17..
 */
public class Bitmap implements AndroidClass {
    private int mWidth;
    private int mHeight;

    private String mInstanceName;

    private Bitmap(int width, int height, Config config) {
        mWidth = width;
        mHeight = height;

        mInstanceName = Initializer.generateInstanceName(Bitmap.class);
    }

    public static Bitmap createBitmap(int width, int height, Config bitmapConfig) {
        Bitmap bitmap = new Bitmap(width, height, bitmapConfig);
        OutputBuilder.append("Bitmap %s = Bitmap.createBitmap(%d, %d, Config.%s);", bitmap.mInstanceName, width, height, bitmapConfig.name());
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

    @Override
    public String getInstanceName() {
        return mInstanceName;
    }

    @Override
    public void setInstanceName(String instanceName) {
        mInstanceName = instanceName;
    }

    public enum Config {ARGB_8888}
}
