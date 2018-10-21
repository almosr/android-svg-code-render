package android.util;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Constant array class for storing bytes statically
 *
 * @author Almos Rajnai
 */
public class ByteConstantArray extends AndroidClass {
    private static HashMap<String, ByteConstantArray> mInstances = new HashMap<>();

    private ByteConstantArray(byte[] data) {
        setInstanceName(generateInstanceName(getClass(), true));

        String line = String.format("private static final byte[] %s = %s;", getInstanceName(null), Arrays.toString(data).replace("[", "{").replace("]", "f}").replace(",", "f,"));
        OutputBuilder.appendConstant(this, line);
    }

    /**
     * Creates a new instance or returns a previous instance with the same data.
     *
     * @param data {@code byte} array for the instance.
     * @return new or previous instance for the data.
     */
    public static ByteConstantArray getInstance(byte[] data) {
        return ConstantArrayUtils.findInstance(Arrays.toString(data), mInstances, new ByteConstantArray(data));
    }
}
