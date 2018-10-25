package android.graphics;

import android.util.FloatConstantArray;
import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Matrix class
 *
 * @author Almos Rajnai
 */
public class Matrix extends AndroidClass {

    public Matrix(Matrix matrix) {
        setParent(matrix);
    }

    public Matrix() {
        init();
    }

    public Matrix(String instanceName) {
        mInstanceName = instanceName;
    }

    public void postConcat(Matrix matrix) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "postConcat", matrix.getInstanceName(this));
    }

    public boolean invert(Matrix matrix) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "invert", "%s", matrix.getInstanceName(this));

        //TODO: matrix invert
        return true;
    }

    public void preConcat(Matrix matrix) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "preConcat", matrix.getInstanceName(this));
    }

    public void mapPoints(float[] points) {
        checkInheritance();
        FloatConstantArray constant = FloatConstantArray.getInstance(points);
        OutputBuilder.appendMethodCall(this, "mapPoints", constant.getInstanceName(this));
    }

    public void preTranslate(float x, float y) {
        checkInheritance();
        if (x != 0.0f || y != 0.0f) {
            OutputBuilder.appendMethodCall(this, "preTranslate", "%ff, %ff", x, y);
        }
    }

    public void preScale(float xScale, float yScale) {
        checkInheritance();
        if (xScale != 1.0f || yScale != 1.0f) {
            OutputBuilder.appendMethodCall(this, "preScale", "%ff, %ff", xScale, yScale);
        }
    }

    public void postScale(float rx, float ry) {
        OutputBuilder.appendMethodCall(this, "postScale", "%ff, %ff", rx, ry);
    }

    public void postRotate(float degrees) {
        OutputBuilder.appendMethodCall(this, "postRotate", "%ff", degrees);
    }

    public void postTranslate(float cx, float cy) {
        OutputBuilder.appendMethodCall(this, "postTranslate", "%ff, %ff", cx, cy);
    }

    public void preRotate(float degrees) {
        OutputBuilder.appendMethodCall(this, "preRotate", "%ff", degrees);
    }

    public void reset() {
        OutputBuilder.appendMethodCall(this, "reset");
    }

    public void preSkew(float kx, float ky) {
        OutputBuilder.appendMethodCall(this, "preSkew", "%ff, %ff", kx, ky);
    }

    public void setValues(float[] values) {
        checkInheritance();
        FloatConstantArray constant = FloatConstantArray.getInstance(values);
        OutputBuilder.appendMethodCall(this, "setValues", constant.getInstanceName(this));
    }

    public void preRotate(float degree, float cx, float cy) {
        OutputBuilder.appendMethodCall(this, "preRotate", "%ff, %ff, %ff", degree, cx, cy);
    }
}
