package android.graphics;

import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android LinearGradient class
 *
 * @author Almos Rajnai
 */
public class LinearGradient extends Shader {

    public LinearGradient(float x1, float y1, float x2, float y2, int[] colours, float[] positions, Shader.TileMode tileMode) {
        OutputBuilder.addImport(Shader.class);

        init(null, "%ff, %ff, %ff, %ff, %s, %s, %s",
                x1, y1, x2, y2,
                OutputBuilder.createArrayParameter(colours, true),
                OutputBuilder.createArrayParameter(positions),
                "Shader.TileMode." + tileMode.name());
    }
}
