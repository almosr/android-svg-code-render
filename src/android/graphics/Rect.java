package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;

/**
 * Created by racs on 2015.03.17..
 */
public class Rect implements AndroidClass {
    public int left;
    public int top;
    public int right;
    public int bottom;

    private String mInstanceName;

    public Rect() {
        Initializer.init(this);
    }

    public Rect(int left, int top, int right, int bottom) {
        this();

        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
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
