package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Canvas class
 *
 * @author Almos Rajnai
 */
public class Canvas extends AndroidClass {
    public static final int HAS_ALPHA_LAYER_SAVE_FLAG = 4;
    public static final int MATRIX_SAVE_FLAG = 1;
    private Matrix mMatrix;
    private int[] flagValues = {MATRIX_SAVE_FLAG, HAS_ALPHA_LAYER_SAVE_FLAG};
    private String[] flagNames = {"MATRIX_SAVE_FLAG", "HAS_ALPHA_LAYER_SAVE_FLAG"};
    private int mWidth;
    private int mHeight;

    //This flag is set if the Canvas is the root class which is referring to all other classes in the output.
    private boolean mRoot;

    public Canvas(String instanceName, int width, int height, boolean isRoot) {
        //This constructor is used only by the setup code for the render method generation
        mInstanceName = instanceName;
        mWidth = width;
        mHeight = height;
        mRoot = isRoot;
    }

    public Canvas(Bitmap bitmap) {
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();

        init("%s", bitmap.getInstanceName(this));
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void save() {
        OutputBuilder.appendMethodCall(this, "save");
    }

    public void restore() {
        OutputBuilder.appendMethodCall(this, "restore");
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
        OutputBuilder.appendMethodCall(this, "setMatrix", "%s", matrix.getInstanceName(this));
    }

    public void drawPath(Path path, Paint fillPaint) {
        OutputBuilder.appendMethodCall(this, "drawPath", "%s, %s", path.getInstanceName(this), fillPaint.getInstanceName(this));
    }

    public void concat(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, "concat", "%s", matrix.getInstanceName(this));
        //Simulate concatenation
        if (matrix != null) {
            mMatrix = new Matrix(generateInstanceName(Matrix.class));
            OutputBuilder.append(mMatrix, "Matrix %s = %s.getMatrix();", mMatrix.getInstanceName(this), getInstanceName(this));
        }
    }

    public void translate(float x, float y) {
        OutputBuilder.appendMethodCall(this, "translate", "%ff, %ff", x, y);

    }

    public void saveLayerAlpha(RectF rectF, int alpha, int saveFlags) {
        if (rectF != null) {
            throw new RuntimeException("RectF instances in generated code is not supported");
        }

        OutputBuilder.appendMethodCall(this, "saveLayerAlpha", "null, %d, %s", alpha, OutputBuilder.splitFlags(saveFlags, "Canvas.", flagValues, flagNames));
    }

    public void drawBitmap(Bitmap maskedContent, float left, float top, Paint paint) {
        OutputBuilder.appendMethodCall(this, "drawBitmap", "%ff, %ff, %s", left, top, paint.getInstanceName(this));
    }

    public void drawText(String text, float x, float y, Paint paint) {
        OutputBuilder.appendMethodCall(this, "drawText", "\"%s\", %ff, %ff, %s", text, x, y, paint.getInstanceName(this));
    }

    public void drawTextOnPath(String text, Path path, float x, float y, Paint paint) {
        OutputBuilder.appendMethodCall(this, "drawTextOnPath", "\"%s\", %s, %ff, %ff, %s", text, paint.getInstanceName(this), x, y, paint.getInstanceName(this));
    }

    public void clipRect(float left, float top, float right, float bottom) {
        OutputBuilder.appendMethodCall(this, "clipRect", "%ff, %ff, %ff, %ff", left, top, right, bottom);
    }

    public void drawColor(int color) {
        OutputBuilder.appendMethodCall(this, "drawColor", "0x%08x", color);
    }

    public void clipPath(Path clipPath) {
        OutputBuilder.appendMethodCall(this, "clipPath", "%s", clipPath.getInstanceName(this));
    }

    public void save(int saveFlags) {
        OutputBuilder.appendMethodCall(this, "save", "%s", OutputBuilder.splitFlags(saveFlags, "Canvas.", flagValues, flagNames));
    }

    public void scale(float width, float height) {
        OutputBuilder.appendMethodCall(this, "scale", "%ff, %ff", width, height);
    }

    @Override
    public boolean isUsed() {
        //Root canvas instamce must not be removed
        return mRoot || super.isUsed();
    }
}
