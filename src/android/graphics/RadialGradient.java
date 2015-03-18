package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class RadialGradient extends Shader {
    private Matrix mLocalMatrix;

    public RadialGradient(float cx, float cy, float r, int[] colours, float[] positions, TileMode tileMode) {
        throw new RuntimeException("Dummy function");
    }

    public Matrix getLocalMatrix() {
        return mLocalMatrix;
    }

    public void setLocalMatrix(Matrix localMatrix) {
        mLocalMatrix = localMatrix;
    }
}
