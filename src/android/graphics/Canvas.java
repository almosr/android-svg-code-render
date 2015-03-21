package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

/**
 * Created by racs on 2015.03.17..
 */
public class Canvas implements AndroidClass {
    public static final int HAS_ALPHA_LAYER_SAVE_FLAG = 4;
    public static final int MATRIX_SAVE_FLAG = 1;
    public String mInstanceName;
    private Matrix mMatrix;
    private int[] flagValues = {MATRIX_SAVE_FLAG, HAS_ALPHA_LAYER_SAVE_FLAG};
    private String[] flagNames = {"MATRIX_SAVE_FLAG", "HAS_ALPHA_LAYER_SAVE_FLAG"};
    private int mWidth;
    private int mHeight;

    public Canvas(String instanceName, int width, int height) {
        //This constructor is used only by the setup code for the render method generation
        mInstanceName = instanceName;
        //TODO: solve dynamic parameters for width and height
        mWidth = width;
        mHeight = height;
    }

    public Canvas(Bitmap bitmap) {
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();

        Initializer.init(this, "%s", bitmap.getInstanceName());
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
        OutputBuilder.appendMethodCall(this, "setMatrix", "%s", matrix.getInstanceName());
    }

    public void drawPath(Path path, Paint fillPaint) {
        OutputBuilder.appendMethodCall(this, "drawPath", "%s, %s", path.getInstanceName(), fillPaint.getInstanceName());
    }

    public void concat(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, "concat", "%s", matrix.getInstanceName());
        //Simulate concatenation
        if (matrix != null) {
            mMatrix = new Matrix(Initializer.generateInstanceName(Matrix.class));
            OutputBuilder.append("Matrix %s = %s.getMatrix();", mMatrix.getInstanceName(), getInstanceName());
        }
    }

    public void translate(float x, float y) {
        OutputBuilder.appendMethodCall(this, "translate", "%ff, %ff", x, y);

    }

    public void saveLayerAlpha(RectF rectF, int alpha, int saveFlags) {
        OutputBuilder.appendMethodCall(this, "saveLayerAlpha", "%s, %d, %s", rectF != null ? rectF.getInstanceName() : "null", alpha, OutputBuilder.splitFlags(saveFlags, "Canvas.", flagValues, flagNames));
    }

    public void drawBitmap(Bitmap maskedContent, float left, float top, Paint paint) {
        OutputBuilder.appendMethodCall(this, "drawBitmap", "%ff, %ff, %s", left, top, paint.getInstanceName());
    }

    public void drawText(String text, float x, float y, Paint paint) {
        OutputBuilder.appendMethodCall(this, "drawText", "\"%s\", %ff, %ff, %s", text, x, y, paint.getInstanceName());
    }

    public void drawTextOnPath(String text, Path path, float x, float y, Paint paint) {
        OutputBuilder.appendMethodCall(this, "drawTextOnPath", "\"%s\", %s, %ff, %ff, %s", text, paint.getInstanceName(), x, y, paint.getInstanceName());
    }

    public void clipRect(float left, float top, float right, float bottom) {
        OutputBuilder.appendMethodCall(this, "clipRect", "%ff, %ff, %ff, %ff", left, top, right, bottom);
    }

    public void drawColor(int color) {
        OutputBuilder.appendMethodCall(this, "drawColor", "0x%08x", color);
    }

    public void clipPath(Path clipPath) {
        OutputBuilder.appendMethodCall(this, "clipPath", "%s", clipPath.getInstanceName());
    }

    public void save(int saveFlags) {
        OutputBuilder.appendMethodCall(this, "save", "%s", OutputBuilder.splitFlags(saveFlags, "Canvas.", flagValues, flagNames));
    }

    public void scale(float width, float height) {
        OutputBuilder.appendMethodCall(this, "scale", "%ff, %ff", width, height);
    }

    @Override
    public String getInstanceName() {
        return mInstanceName;
    }

    @Override
    public void setInstanceName(String instanceName) {
        mInstanceName = instanceName;
    }
}
