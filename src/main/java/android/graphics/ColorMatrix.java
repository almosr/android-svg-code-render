package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

public class ColorMatrix extends AndroidClass {
    public ColorMatrix(float[] src) {
        init("%s", OutputBuilder.dumpArray(src));
    }
}