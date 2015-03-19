package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;

/**
 * Created by racs on 2015.03.17..
 */
public class RectF implements AndroidClass {
    public float left;
    public float top;
    public float right;
    public float bottom;

    private String mInstanceName;

    public RectF(float left, float top, float right, float bottom) {
        this();

        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public RectF() {
        Initializer.init(this);
    }

    public RectF(Rect rect) {
        throw new RuntimeException("Dummy function");
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return top - bottom;
    }

    public void offset(float x, float y) {
        throw new RuntimeException("Dummy function");
    }

    public void union(RectF bounds) {
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
}
