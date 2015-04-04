package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Matrix class
 *
 * @author Almos Rajnai
 */
public class Matrix extends AndroidClass {

    public Matrix(Matrix matrix) {
        throw new RuntimeException("Dummy function");
    }

    public Matrix() {
        init(null);
    }

    public Matrix(String instanceName) {
        mInstanceName = instanceName;
    }

    public void postConcat(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, matrix), "postConcat", matrix.getInstanceName(this));
    }

    public boolean invert(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, matrix), "invert", "%s", matrix.getInstanceName(this));

        //TODO: matrix invert
        return true;
    }

    public void preConcat(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, matrix), "preConcat", matrix.getInstanceName(this));
    }

    public void mapPoints(float[] points) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "mapPoints", OutputBuilder.createArrayParameter(points));
    }

    public void preTranslate(float x, float y) {
        if (x != 0.0f || y != 0.0f) {
            OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "preTranslate", "%ff, %ff", x, y);
        }
    }

    public void preScale(float xScale, float yScale) {
        if (xScale != 1.0f || yScale != 1.0f) {
            OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "preScale", "%ff, %ff", xScale, yScale);
        }
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
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setValues", OutputBuilder.createArrayParameter(values));
    }

    public void preRotate(float degree, float cx, float cy) {
        throw new RuntimeException("Dummy function");
    }
}
