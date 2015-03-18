package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class Bitmap {
    private int mWidth;
    private int mHeight;

    public static Bitmap createBitmap(int width, int height, Config bitmapConfig) {
        throw new RuntimeException("Dummy function");
    }

    public void recycle() {
        throw new RuntimeException("Dummy function");
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
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
