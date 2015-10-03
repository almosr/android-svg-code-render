package android.graphics;

import android.util.FloatConstantArray;
import android.util.IntConstantArray;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android RadialGradient class
 *
 * @author Almos Rajnai
 */
public class RadialGradient extends Shader {

    public RadialGradient(float cx, float cy, float r, int[] colours, float[] positions, TileMode tileMode) {
        OutputBuilder.addImport(Shader.class);

        IntConstantArray colourConst = new IntConstantArray(colours, true);
        FloatConstantArray posConst = new FloatConstantArray(positions);

        init("%ff, %ff, %ff, %s, %s, %s",
                cx, cy, r,
                colourConst.getInstanceName(this),
                posConst.getInstanceName(this),
                "Shader.TileMode." + tileMode.name());
    }
}
