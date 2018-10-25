package android.graphics;

import android.os.Build;
import android.util.Log;
import android_svg_code_render.*;

/**
 * Simulated Android Canvas class
 *
 * @author Almos Rajnai
 */
public class Canvas extends AndroidClass {
    public static final int MATRIX_SAVE_FLAG = 1;
    public static final int ALL_SAVE_FLAG = 31;

    private static final String TAG = Canvas.class.getName();
    private final int mWidth;
    private final int mHeight;
    private Matrix mMatrix;
    //This flag is set if the Canvas is the root class which is referring to all other classes in the output.
    private boolean mRoot;

    public Canvas(String instanceName, int width, int height, boolean isRoot) {
        //This constructor is used only by the setup code for the render method generation
        mInstanceName = instanceName;
        mWidth = width;
        mHeight = height;
        mRoot = isRoot;
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
        if (mMatrix == null) {
            Log.w(TAG, "Inner Matrix instance is not initialized yet when read from Canvas");

            //Get Matrix instance from the real Canvas and use that instance
            getMatrixInstanceFromCanvas();
        }

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
            getMatrixInstanceFromCanvas();
        }
    }

    private void getMatrixInstanceFromCanvas() {
        mMatrix = new Matrix(generateInstanceName(Matrix.class));
        OutputBuilder.appendInit(mMatrix, null);
        OutputBuilder.append(mMatrix, "%s = %s.getMatrix();", mMatrix.getInstanceName(this), getInstanceName(this));
    }

    public void translate(float x, float y) {
        OutputBuilder.appendMethodCall(this, "translate", "%ff, %ff", x, y);

    }

    public void saveLayerAlpha(RectF rectF, int alpha, int saveFlags) {
        if (rectF != null) {
            throw new RuntimeException("RectF instances in generated code is not supported");
        }

        if (Build.VERSION.SDK_INT >= 21) {
            //From Lollipop we can use the flag-less version of the method
            OutputBuilder.appendMethodCall(this, "saveLayerAlpha", "null, %d", alpha);
        } else {
            //Only ALL_SAVE_FLAG is available from Android P, all other flags were deprecated,
            //so flags will be ignored and substituted with Canvas.ALL_SAVE_FLAG
            OutputBuilder.appendMethodCall(this, "saveLayerAlpha", "null, %d, %s", alpha, "Canvas.ALL_SAVE_FLAG");
        }
    }

    public void drawBitmap(Bitmap maskedContent, float left, float top, Paint paint) {
        OutputBuilder.appendMethodCall(this, "drawBitmap", "%s, %ff, %ff, %s", maskedContent.getInstanceName(this), left, top, paint.getInstanceName(this));
    }

    public void drawText(String text, float x, float y, Paint paint) {
        if (!text.isEmpty()) {
            OutputBuilder.appendMethodCall(this, "drawText", "%s, %ff, %ff, %s", TextReplacements.findTextReplacement(text), x, y, paint.getInstanceName(this));
        }
    }

    public void drawTextOnPath(String text, Path path, float x, float y, Paint paint) {
        Main.checkAPIWarning("Canvas.drawTextOnPath", 16);
        if (!text.isEmpty()) {
            OutputBuilder.appendMethodCall(this, "drawTextOnPath", "%s, %s, %ff, %ff, %s", TextReplacements.findTextReplacement(text), path.getInstanceName(this), x, y, paint.getInstanceName(this));
        }
    }

    public void clipRect(float left, float top, float right, float bottom) {
        Main.checkAPIWarning("Canvas.clipRect with rotation/perspective", 16);
        OutputBuilder.appendMethodCall(this, "clipRect", "%ff, %ff, %ff, %ff", left, top, right, bottom);
    }

    public void drawColor(int color) {
        OutputBuilder.appendMethodCall(this, "drawColor", "%s", ColorReplacements.findColorReplacement(color));
    }

    public void clipPath(Path clipPath) {
        Main.checkAPIWarning("Canvas.clipPath", 18);
        OutputBuilder.appendMethodCall(this, "clipPath", "%s", clipPath.getInstanceName(this));
    }

    public void save(int saveFlags) {
        //As of Android P save takes no parameters
        OutputBuilder.appendMethodCall(this, "save");
    }

    public void scale(float width, float height) {
        OutputBuilder.appendMethodCall(this, "scale", "%ff, %ff", width, height);
    }

    @Override
    public boolean isUsed() {
        //Root canvas instance must not be removed
        return mRoot || super.isUsed();
    }

    public void saveLayer(RectF bounds, Paint paint, int saveFlags) {
        if (bounds != null) {
            throw new RuntimeException("Canvas.saveLayer() with non-null bounds is not supported");
        }

        //Only ALL_SAVE_FLAG is available from Android P
        OutputBuilder.appendMethodCall(this, "saveLayer", "null, %s, %s",
                paint.getInstanceName(this),
                "Canvas.ALL_SAVE_FLAG");
    }
}
