package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class Matrix {
    public Matrix(Matrix matrix) {
        throw new RuntimeException("Dummy function");
    }

    public Matrix() {

    }

    public void postConcat(Matrix matrix) {

    }

    public boolean invert(Matrix matrix) {
        throw new RuntimeException("Dummy function");
    }

    public void preConcat(Matrix matrix) {
        throw new RuntimeException("Dummy function");
    }

    public void mapPoints(float[] points) {
        throw new RuntimeException("Dummy function");
    }

    public void preTranslate(float x, float y) {
        throw new RuntimeException("Dummy function");
    }

    public void preScale(float xScale, float yScale) {
        throw new RuntimeException("Dummy function");
    }

    public void postScale(float rx, float ry) {
        throw new RuntimeException("Dummy function");
    }

    public void postRotate(float degrees) {
        throw new RuntimeException("Dummy function");
    }

    public void postTranslate(float cx, float cy) {
        throw new RuntimeException("Dummy function");
    }

    public void preRotate(float degrees) {
        throw new RuntimeException("Dummy function");
    }

    public void reset() {
        throw new RuntimeException("Dummy function");
    }

    public void preSkew(float kx, float ky) {
        throw new RuntimeException("Dummy function");
    }

    public void setValues(float[] values) {
        throw new RuntimeException("Dummy function");
    }

    public void preRotate(float degree, float cx, float cy) {
        throw new RuntimeException("Dummy function");
    }
}
