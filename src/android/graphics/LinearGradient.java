package android.graphics;

import android.util.FloatConstantArray;
import android.util.IntConstantArray;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android LinearGradient class
 *
 * @author Almos Rajnai
 */
public class LinearGradient extends Shader {

    public LinearGradient(float x1, float y1, float x2, float y2, int[] colours, float[] positions, Shader.TileMode tileMode) {
        OutputBuilder.addImport(Shader.class);

        IntConstantArray colourConst = IntConstantArray.getInstance(colours);
        FloatConstantArray posConst = FloatConstantArray.getInstance(positions);

        init("%ff, %ff, %ff, %ff, %s, %s, %s",
                x1, y1, x2, y2,
                colourConst.getInstanceName(this),
                posConst.getInstanceName(this),
                "Shader.TileMode." + tileMode.name());
    }
}
