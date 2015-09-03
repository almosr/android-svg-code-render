package android.graphics;

import android.util.Log;
import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

import java.util.ArrayList;

/**
 * Simulated Android Canvas class
 *
 * @author Almos Rajnai
 */
public class Canvas extends AndroidClass {
    private static final String TAG = Canvas.class.getName();

    public static final int HAS_ALPHA_LAYER_SAVE_FLAG = 4;
    public static final int MATRIX_SAVE_FLAG = 1;
    private Matrix mMatrix;
    private int[] flagValues = {MATRIX_SAVE_FLAG, HAS_ALPHA_LAYER_SAVE_FLAG};
    private String[] flagNames = {"MATRIX_SAVE_FLAG", "HAS_ALPHA_LAYER_SAVE_FLAG"};
    private int mWidth;
    private int mHeight;

    private ArrayList<TextReplacement> mTextReplacements = new ArrayList<>();

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

        init(OutputBuilder.dependencyList(bitmap), "%s", bitmap.getInstanceName(this));
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void save() {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "save");
    }

    public void restore() {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "restore");
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
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, matrix), "setMatrix", "%s", matrix.getInstanceName(this));
    }

    public void drawPath(Path path, Paint fillPaint) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, path, fillPaint), "drawPath", "%s, %s", path.getInstanceName(this), fillPaint.getInstanceName(this));
    }

    public void concat(Matrix matrix) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, matrix), "concat", "%s", matrix.getInstanceName(this));
        //Simulate concatenation
        if (matrix != null) {
            getMatrixInstanceFromCanvas();
        }
    }

    private void getMatrixInstanceFromCanvas() {
        mMatrix = new Matrix(generateInstanceName(Matrix.class));
        OutputBuilder.append(mMatrix, mMatrix, OutputBuilder.dependencyList(this), "Matrix %s = %s.getMatrix();", mMatrix.getInstanceName(this), getInstanceName(this));
    }

    public void translate(float x, float y) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "translate", "%ff, %ff", x, y);

    }

    public void saveLayerAlpha(RectF rectF, int alpha, int saveFlags) {
        if (rectF != null) {
            throw new RuntimeException("RectF instances in generated code is not supported");
        }

        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "saveLayerAlpha", "null, %d, %s", alpha, OutputBuilder.splitFlags(saveFlags, "Canvas.", flagValues, flagNames));
    }

    public void drawBitmap(Bitmap maskedContent, float left, float top, Paint paint) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, maskedContent, paint), "drawBitmap", "%s, %ff, %ff, %s", maskedContent.getInstanceName(this), left, top, paint.getInstanceName(this));
    }

    public void drawText(String text, float x, float y, Paint paint) {
        if (!text.isEmpty()) {
            OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, paint), "drawText", "%s, %ff, %ff, %s", findTextReplacement(text), x, y, paint.getInstanceName(this));
        }
    }

    public void drawTextOnPath(String text, Path path, float x, float y, Paint paint) {
        if (!text.isEmpty()) {
            OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, paint), "drawTextOnPath", "%s, %s, %ff, %ff, %s", findTextReplacement(text), path.getInstanceName(this), x, y, paint.getInstanceName(this));
        }
    }

    public void clipRect(float left, float top, float right, float bottom) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "clipRect", "%ff, %ff, %ff, %ff", left, top, right, bottom);
    }

    public void drawColor(int color) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "drawColor", "0x%08x", color);
    }

    public void clipPath(Path clipPath) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, clipPath), "clipPath", "%s", clipPath.getInstanceName(this));
    }

    public void save(int saveFlags) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "save", "%s", OutputBuilder.splitFlags(saveFlags, "Canvas.", flagValues, flagNames));
    }

    public void scale(float width, float height) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "scale", "%ff, %ff", width, height);
    }

    @Override
    public boolean isUsed() {
        //Root canvas instamce must not be removed
        return mRoot || super.isUsed();
    }

    public void addTextReplacement(String text, String variable) {
        mTextReplacements.add(new TextReplacement(text, variable));
    }

    private String findTextReplacement(String text) {
        for (TextReplacement textReplacement : mTextReplacements) {
            if (textReplacement.text.equals(text)) {
                textReplacement.used = true;
                return textReplacement.parameter;
            }
        }

        return String.format("\"%s\"", text);
    }

    public void verifyReplacementTextUsage() {
        for (TextReplacement textReplacement : mTextReplacements) {
            if (!textReplacement.used) {
                Log.w(TAG, String.format("Text for replacement was not found: '%s'", textReplacement.text));
            }
        }
    }

    private class TextReplacement {
        String text;
        String parameter;
        boolean used;

        public TextReplacement(String text, String parameter) {
            this.text = text;
            this.parameter = parameter;
        }
    }
}
