package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Shader class
 *
 * @author Almos Rajnai
 */
public class Shader extends AndroidClass {
    private Matrix mLocalMatrix;

    public void getLocalMatrix(Matrix localM) {
        if (localM != null) {
            mLocalMatrix = localM;
        }
    }

    public void setLocalMatrix(Matrix localM) {
        mLocalMatrix = localM;
        OutputBuilder.appendMethodCall(this, "setLocalMatrix", "%s", localM.getInstanceName(this));
    }

    public enum TileMode {
        MIRROR, REPEAT, CLAMP
    }
}
