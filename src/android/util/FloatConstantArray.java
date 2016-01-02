package android.util;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Constant array class for storing floats statically
 *
 * @author Almos Rajnai
 */
public class FloatConstantArray extends AndroidClass {

    private static HashMap<String, FloatConstantArray> mInstances = new HashMap<>();

    private FloatConstantArray(float[] data) {
        setInstanceName(generateInstanceName(getClass(), true));

        String line = String.format("private static final float[] %s = %s;", getInstanceName(null), Arrays.toString(data).replace("[", "{").replace("]", "f}").replace(",", "f,"));
        OutputBuilder.appendConstant(this, line);
    }

    /**
     * Creates a new instance or returns a previous instance with the same data.
     *
     * @param data {@code float} array for the instance.
     * @return new or previous instance for the data.
     */
    public static FloatConstantArray getInstance(float[] data) {
        return ConstantArrayUtils.findInstance(Arrays.toString(data), mInstances, new FloatConstantArray(data));
    }
}
