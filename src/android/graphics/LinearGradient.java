package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class LinearGradient extends Shader {
    private Matrix mLocalMatrix;

    public LinearGradient(float x1, float y1, float x2, float y2, int[] colours, float[] positions, Shader.TileMode tileMode) {
        throw new RuntimeException("Dummy function");
    }

    public Matrix getLocalMatrix() {
        return mLocalMatrix;
    }

    public void setLocalMatrix(Matrix localMatrix) {
        mLocalMatrix = localMatrix;
    }
}
