package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Shader class
 *
 * @author Almos Rajnai
 */
public class Shader extends AndroidClass {

    public void getLocalMatrix(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, matrix), "getLocalMatrix", "%s", matrix.getInstanceName(this));
    }

    public void setLocalMatrix(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, matrix), "setLocalMatrix", "%s", matrix.getInstanceName(this));
    }

    public enum TileMode {
        MIRROR, REPEAT, CLAMP
    }
}
