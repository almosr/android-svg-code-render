package android.graphics;

import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

/**
 * Created by racs on 2015.03.17..
 */
public class LinearGradient extends Shader {

    public LinearGradient(float x1, float y1, float x2, float y2, int[] colours, float[] positions, Shader.TileMode tileMode) {
        Initializer.init(this, String.format("%ff, %ff, %ff, %ff, %s, %s, %s",
                x1, y1, x2, y2,
                OutputBuilder.createArrayParameter(colours),
                OutputBuilder.createArrayParameter(positions),
                "Shader.TileMode." + tileMode.name()));
    }
}
