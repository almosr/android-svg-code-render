package android.graphics;

public class ColorMatrixColorFilter extends ColorFilter {
    public ColorMatrixColorFilter(ColorMatrix matrix) {
        init("%s", matrix.getInstanceName(this));
    }
}