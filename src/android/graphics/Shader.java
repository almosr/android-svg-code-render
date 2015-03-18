package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class Shader {
    private Matrix mLocalMatrix;

    public Matrix getLocalMatrix(Matrix currentShaderMatrix) {
        throw new RuntimeException("Dummy function");
    }

    public void setLocalMatrix(Matrix localMatrix) {
        mLocalMatrix = localMatrix;
    }

    public enum TileMode {
        MIRROR, REPEAT, CLAMP

    }
}
