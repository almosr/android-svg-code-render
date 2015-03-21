package android.graphics;

import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

/**
 * Created by racs on 2015.03.17..
 */
public class RadialGradient extends Shader {
    private Matrix mLocalMatrix;

    public RadialGradient(float cx, float cy, float r, int[] colours, float[] positions, TileMode tileMode) {
        OutputBuilder.addImport(Shader.class);

        Initializer.init(this, String.format("%ff, %ff, %ff, %s, %s, %s",
                cx, cy, r,
                OutputBuilder.createArrayParameter(colours, true),
                OutputBuilder.createArrayParameter(positions),
                "Shader.TileMode." + tileMode.name()));
    }

    public Matrix getLocalMatrix() {
        return mLocalMatrix;
    }

    public void setLocalMatrix(Matrix localMatrix) {
        mLocalMatrix = localMatrix;
    }
}
