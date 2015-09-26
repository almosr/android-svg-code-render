package android.util;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Constant array class for storing ints statically
 *
 * @author Almos Rajnai
 */
public class IntConstantArray extends AndroidClass {

    public IntConstantArray(int[] data, boolean hexFormat) {
        setInstanceName(generateInstanceName(getClass(), true));

        String numFormat = hexFormat ? "0x%08x" : "%d";
        StringBuilder line = new StringBuilder();
        line.append("private static final int[]   ");
        line.append(getInstanceName(null));
        line.append(" = {");
        for (int i = 0; i < data.length; i++) {
            line.append(String.format(numFormat, data[i]));
            if (i != data.length - 1) {
                line.append(", ");
            }
        }
        line.append("};");

        OutputBuilder.appendConstant(this, line.toString());
    }
}
