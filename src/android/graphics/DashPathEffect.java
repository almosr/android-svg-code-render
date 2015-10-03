package android.graphics;

import android.util.FloatConstantArray;
import android_svg_code_render.OutputBuilder;

/**
 * Created by racs on 2015.03.17..
 */
public class DashPathEffect extends PathEffect {
    public DashPathEffect(float[] intervals, float offset) {
        OutputBuilder.addImport(Shader.class);

        FloatConstantArray intervalsConst = new FloatConstantArray(intervals);

        init("%s, %ff",
                intervalsConst.getInstanceName(this),
                offset);
    }
}
