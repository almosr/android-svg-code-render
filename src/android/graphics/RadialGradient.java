package android.graphics;

import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android RadialGradient class
 *
 * @author Almos Rajnai
 */
public class RadialGradient extends Shader {

    public RadialGradient(float cx, float cy, float r, int[] colours, float[] positions, TileMode tileMode) {
        OutputBuilder.addImport(Shader.class);

        init("%ff, %ff, %ff, %s, %s, %s",
                cx, cy, r,
                OutputBuilder.createArrayParameter(colours, true),
                OutputBuilder.createArrayParameter(positions),
                "Shader.TileMode." + tileMode.name());
    }
}
