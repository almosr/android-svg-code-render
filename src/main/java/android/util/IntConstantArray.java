package android.util;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Constant array class for storing ints statically
 *
 * @author Almos Rajnai
 */
public class IntConstantArray extends AndroidClass {
    private static HashMap<String, IntConstantArray> mInstances = new HashMap<>();

    private IntConstantArray(int[] data) {
        setInstanceName(generateInstanceName(getClass(), true));

        StringBuilder line = new StringBuilder();
        line.append("private static final int[]   ");
        line.append(getInstanceName(null));
        line.append(" = {");
        for (int i = 0; i < data.length; i++) {
            line.append(String.format("0x%08x", data[i]));
            if (i != data.length - 1) {
                line.append(", ");
            }
        }
        line.append("};");

        OutputBuilder.appendConstant(this, line.toString());
    }

    /**
     * Creates a new instance or returns a previous instance with the same data.
     *
     * @param data {@code int} array for the instance.
     * @return new or previous instance for the data.
     */
    public static IntConstantArray getInstance(int[] data) {
        return ConstantArrayUtils.findInstance(Arrays.toString(data), mInstances, new IntConstantArray(data));
    }
}
