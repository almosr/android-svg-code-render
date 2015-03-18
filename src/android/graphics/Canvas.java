package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class Canvas {
    public static final int HAS_ALPHA_LAYER_SAVE_FLAG = 4;
    public static final int MATRIX_SAVE_FLAG = 1;

    private Matrix mMatrix;

    public Canvas(Bitmap bitmap) {
        throw new RuntimeException("Dummy function");
    }

    public int getWidth() {
        throw new RuntimeException("Dummy function");
    }

    public int getHeight() {
        throw new RuntimeException("Dummy function");
    }

    public void save() {
        throw new RuntimeException("Dummy function");
    }

    public void restore() {
        throw new RuntimeException("Dummy function");
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
    }

    public void drawPath(Path path, Paint fillPaint) {
        throw new RuntimeException("Dummy function");
    }

    public void concat(Matrix matrix) {
        throw new RuntimeException("Dummy function");
    }

    public void translate(float x, float y) {
        throw new RuntimeException("Dummy function");

    }

    public void saveLayerAlpha(RectF rectF, int alpha, int hasAlphaLayerSaveFlag) {
        throw new RuntimeException("Dummy function");
    }

    public void drawBitmap(Bitmap maskedContent, float left, float top, Paint paint) {
        throw new RuntimeException("Dummy function");

    }

    public void drawText(String text, float x, float y, Paint paint) {
        throw new RuntimeException("Dummy function");
    }

    public void drawTextOnPath(String text, Path path, float x, float y, Paint paint) {
        throw new RuntimeException("Dummy function");
    }

    public void clipRect(float left, float top, float right, float bottom) {
        throw new RuntimeException("Dummy function");
    }

    public void drawColor(int color) {
        throw new RuntimeException("Dummy function");
    }

    public void clipPath(Path clipPath) {
        throw new RuntimeException("Dummy function");
    }

    public void save(int saveFlags) {
        throw new RuntimeException("Dummy function");
    }

    public void scale(float width, float height) {
        throw new RuntimeException("Dummy function");
    }
}
