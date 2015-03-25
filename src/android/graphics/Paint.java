package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.Initializer;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Paint class
 *
 * @author Almos Rajnai
 */
public class Paint implements AndroidClass {
    public static final int ANTI_ALIAS_FLAG = 1;
    public static final int DEV_KERN_TEXT_FLAG = 2;
    public static final int SUBPIXEL_TEXT_FLAG = 4;

    private static int[] flagValues = {ANTI_ALIAS_FLAG, DEV_KERN_TEXT_FLAG, SUBPIXEL_TEXT_FLAG};
    private static String[] flagNames = {"ANTI_ALIAS_FLAG", "DEV_KERN_TEXT_FLAG", "SUBPIXEL_TEXT_FLAG"};
    private float mTextSize;
    private Shader mShader;
    private float mStrokeWidth;
    private Cap mStrokeCap;
    private Join mStrokeJoin;
    private Float mStrokeMiter;
    private PathEffect mPathEffect;
    private boolean mStrikeThruText;
    private boolean mUnderlineText;
    private Typeface mTypeface;
    private int mColor;

    private Paint mParent;

    private String mInstanceName;

    public Paint(Paint paint) {
        //Inherited Paint instance is not initialized in the output unless it is used by through other functions
        mParent = paint;

        //Use the same name as the source Paint object unless something changes the fields from the outside
        mInstanceName = paint.mInstanceName;
    }

    public Paint() {
        Initializer.init(this);
    }

    public void setFlags(int flags) {
        checkInheritance();
        String flagString = OutputBuilder.splitFlags(flags, "Paint.", flagValues, flagNames);
        OutputBuilder.appendMethodCall(this, "setFlags", "%s", flagString);
    }

    public void setStyle(Style style) {
        checkInheritance();
        OutputBuilder.addImport(Paint.class);
        OutputBuilder.appendMethodCall(this, "setStyle", "Paint.Style.%s", style.name());
    }

    public void setTypeface(int typeface) {
        throw new RuntimeException("Dummy function");
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        checkInheritance();
        mTextSize = textSize;
        OutputBuilder.appendMethodCall(this, "setTextSize", "%ff", textSize);
    }

    public Shader getShader() {
        return mShader;
    }

    public void setShader(Shader shader) {
        checkInheritance();
        mShader = shader;
        OutputBuilder.appendMethodCall(this, "setShader", "%s", shader.getInstanceName());
    }

    public float measureText(String text) {
        //TODO: measureText method in Paint
        return 0.0f;
    }

    public void getTextBounds(String text, int start, int count, Rect rect) {
        //TODO: getTextBounds method in Paint
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        checkInheritance();
        mStrokeWidth = strokeWidth;
        OutputBuilder.appendMethodCall(this, "setStrokeWidth", "%ff", strokeWidth);
    }

    public void setStrokeCap(Cap strokeCap) {
        checkInheritance();
        mStrokeCap = strokeCap;
        OutputBuilder.appendMethodCall(this, "setStrokeCap", "Paint.Cap.%s", strokeCap.name());
    }

    public void setStrokeJoin(Join strokeJoin) {
        checkInheritance();
        mStrokeJoin = strokeJoin;
        OutputBuilder.appendMethodCall(this, "setStrokeJoin", "Paint.Join.%s", strokeJoin.name());
    }

    public Float getStrokeMiter() {
        return mStrokeMiter;
    }

    public void setStrokeMiter(Float strokeMiter) {
        checkInheritance();
        mStrokeMiter = strokeMiter;
        OutputBuilder.appendMethodCall(this, "setStrokeMiter", "%ff", strokeMiter);
    }

    public PathEffect getPathEffect() {
        return mPathEffect;
    }

    public void setPathEffect(PathEffect pathEffect) {
        checkInheritance();
        mPathEffect = pathEffect;
        if (pathEffect != null) {
            OutputBuilder.addImport(pathEffect.getClass());
            OutputBuilder.appendMethodCall(this, "setPathEffect", "%s", pathEffect.getClass().getSimpleName());
        } else {
            OutputBuilder.appendMethodCall(this, "setPathEffect", "null");
        }
    }

    public boolean isStrikeThruText() {
        return mStrikeThruText;
    }

    public void setStrikeThruText(boolean strikeThruText) {
        checkInheritance();
        mStrikeThruText = strikeThruText;
    }

    public boolean isUnderlineText() {
        return mUnderlineText;
    }

    public void setUnderlineText(boolean underlineText) {
        checkInheritance();
        mUnderlineText = underlineText;
        OutputBuilder.appendMethodCall(this, "setUnderlineText", "%b", underlineText);
    }

    public void setTypeface(Typeface typeface) {
        checkInheritance();
        OutputBuilder.addImport(Typeface.class);
        OutputBuilder.appendMethodCall(this, "setTypeface", "%s", typeface.getInstanceName());
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        checkInheritance();
        mColor = color;
        OutputBuilder.appendMethodCall(this, "setColor", "0x%08x", color);
    }

    public void getTextPath(String text, int start, int end, float x, float y, Path path) {
        throw new RuntimeException("Dummy function");
    }

    @Override
    public String getInstanceName() {
        return mInstanceName;
    }

    @Override
    public void setInstanceName(String instanceName) {
        mInstanceName = instanceName;
    }

    public enum Style {
        STROKE, FILL
    }

    private void checkInheritance() {
        //This function is called when a property is set from the outside.
        //If the instance is "inherited", that means it was not initialized and represents the same
        //Paint instance which was used in the constructor as parameter.
        //But since this instance will be changed now we need a real new Paint instance, so we create it now.
        if (mParent != null) {
            Initializer.init(this, mParent.getInstanceName());
            mParent = null;
        }
    }

    public enum Cap {ROUND, SQUARE, BUTT}

    public enum Join {ROUND, BEVEL, MITER}
}
