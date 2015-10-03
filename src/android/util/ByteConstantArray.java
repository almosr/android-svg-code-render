package android.util;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

import java.util.Arrays;

/**
 * Constant array class for storing bytes statically
 *
 * @author Almos Rajnai
 */
public class ByteConstantArray extends AndroidClass {

    public ByteConstantArray(byte[] data) {
        setInstanceName(generateInstanceName(getClass(), true));

        String line = String.format("private static final byte[] %s = %s;", getInstanceName(null), Arrays.toString(data).replace("[", "{").replace("]", "f}").replace(",", "f,"));
        OutputBuilder.appendConstant(this, line);
    }
}
