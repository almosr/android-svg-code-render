package android.util;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

import java.util.Arrays;

/**
 * Constant array class for storing floats statically
 *
 * @author Almos Rajnai
 */
public class FloatConstantArray extends AndroidClass {

    public FloatConstantArray(float[] data) {
        setInstanceName(generateInstanceName(getClass(), true));

        String line = String.format("private static final float[] %s = %s;", getInstanceName(null), Arrays.toString(data).replace("[", "{").replace("]", "f}").replace(",", "f,"));
        OutputBuilder.appendConstant(this, line);
    }
}
