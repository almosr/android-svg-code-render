package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

/**
 * Created by racs on 2015.03.17..
 */
public class Shader implements AndroidClass {
    private Matrix mLocalMatrix;

    private String mInstanceName;

    public void getLocalMatrix(Matrix localM) {
        if (localM != null) {
            mLocalMatrix = localM;
        }
    }

    public void setLocalMatrix(Matrix localM) {
        mLocalMatrix = localM;
        OutputBuilder.appendMethodCall(this, "setLocalMatrix", "%s", localM.getInstanceName());
    }

    @Override
    public String getInstanceName() {
        return mInstanceName;
    }

    @Override
    public void setInstanceName(String instanceName) {
        mInstanceName = instanceName;
    }

    public enum TileMode {
        MIRROR, REPEAT, CLAMP
    }
}
