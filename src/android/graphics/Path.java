package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class Path {
    private Path.FillType mFillType;

    public void transform(Matrix currentMatrix, Path transformedPath) {
        throw new RuntimeException("Dummy function");
    }

    public Path.FillType getFillType() {
        return mFillType;
    }

    public void setFillType(Path.FillType fillType) {
        mFillType = fillType;
    }

    public void computeBounds(RectF bounds, boolean exact) {
        throw new RuntimeException("Dummy function");
    }

    public void transform(Matrix transform) {
        throw new RuntimeException("Dummy function");
    }

    public void moveTo(float x, float y) {
        throw new RuntimeException("Dummy function");
    }

    public void lineTo(float x, float y) {
        throw new RuntimeException("Dummy function");
    }

    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        throw new RuntimeException("Dummy function");
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        throw new RuntimeException("Dummy function");
    }

    public void close() {
        throw new RuntimeException("Dummy function");
    }

    public void addPath(Path path, Matrix matrix) {
        throw new RuntimeException("Dummy function");
    }

    public void addPath(Path src) {
        throw new RuntimeException("Dummy function");
    }

    public enum FillType {EVEN_ODD, WINDING}
}
