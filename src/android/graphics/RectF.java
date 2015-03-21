package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

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
        Initializer.init(this, String.format("%ff, %ff, %ff, %ff", left, top, right, bottom));
        initFields(left, top, right, bottom);
    }

    public RectF() {
        Initializer.init(this);
    }

    public RectF(Rect rect) {
        Initializer.init(this, String.format("%s", rect.getInstanceName()));
        initFields(rect.left, rect.top, rect.right, rect.bottom);
    }

    private void initFields(float left, float top, float right, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return top - bottom;
    }

    public void offset(float x, float y) {
        OutputBuilder.appendMethodCall(this, "offset", "%ff, %ff", x, y);
        left += x;
        right += x;
        top += y;
        bottom += y;
    }

    public void union(RectF bounds) {
        OutputBuilder.appendMethodCall(this, "union", "%s", bounds.getInstanceName());

        //Direct copy from Android sources: http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.2_r1/android/graphics/Rect.java#Rect.union%28int%2Cint%2Cint%2Cint%29
        if ((left < right) && (top < bottom)) {
            if ((this.left < this.right) && (this.top < this.bottom)) {
                if (this.left > left) this.left = left;
                if (this.top > top) this.top = top;
                if (this.right < right) this.right = right;
                if (this.bottom < bottom) this.bottom = bottom;
            } else {
                this.left = left;
                this.top = top;
                this.right = right;
                this.bottom = bottom;
            }
        }
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
