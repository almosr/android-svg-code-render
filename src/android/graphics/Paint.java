package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.ColorReplacements;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Paint class
 *
 * @author Almos Rajnai
 */
public class Paint extends AndroidClass {
    public static final int ANTI_ALIAS_FLAG = 1;
    public static final int DEV_KERN_TEXT_FLAG = 2;
    public static final int SUBPIXEL_TEXT_FLAG = 4;
    private static final int[] FLAG_VALUES = {ANTI_ALIAS_FLAG, DEV_KERN_TEXT_FLAG, SUBPIXEL_TEXT_FLAG};
    private static final String[] FLAG_NAMES = {"ANTI_ALIAS_FLAG", "DEV_KERN_TEXT_FLAG", "SUBPIXEL_TEXT_FLAG"};

    private float mTextSize;
    private Shader mShader;

    private Paint mParent;

    public Paint(Paint paint) {
        //Inherited Paint instance is not initialized in the output unless it is used by through other functions
        mParent = paint;

        //Use the same name as the source Paint object unless something changes the fields from the outside
        mInstanceName = paint.mInstanceName;
    }

    public Paint() {
        init(null);
    }

    public void setFlags(int flags) {
        checkInheritance();
        String flagString = OutputBuilder.splitFlags(flags, "Paint.", FLAG_VALUES, FLAG_NAMES);
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setFlags", "%s", flagString);
    }

    public void setStyle(Style style) {
        checkInheritance();
        OutputBuilder.addImport(Paint.class);
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setStyle", "Paint.Style.%s", style.name());
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        checkInheritance();
        mTextSize = textSize;
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setTextSize", "%ff", textSize);
    }

    public Shader getShader() {
        return mShader;
    }

    public void setShader(Shader shader) {
        checkInheritance();
        mShader = shader;
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, shader), "setShader", "%s", shader.getInstanceName(this));
    }

    public float measureText(String text) {
        //TODO: measureText method in Paint
        return 0.0f;
    }

    public void getTextBounds(String text, int start, int count, Rect rect) {
        //TODO: getTextBounds method in Paint
    }

    public void setStrokeWidth(float strokeWidth) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setStrokeWidth", "%ff", strokeWidth);
    }

    public void setStrokeCap(Cap strokeCap) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setStrokeCap", "Paint.Cap.%s", strokeCap.name());
    }

    public void setStrokeJoin(Join strokeJoin) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setStrokeJoin", "Paint.Join.%s", strokeJoin.name());
    }

    public void setStrokeMiter(Float strokeMiter) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setStrokeMiter", "%ff", strokeMiter);
    }

    public void setPathEffect(PathEffect pathEffect) {
        checkInheritance();
        if (pathEffect != null) {
            OutputBuilder.addImport(pathEffect.getClass());
            OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setPathEffect", "%s", pathEffect.getClass().getSimpleName());
        } else {
            OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setPathEffect", "null");
        }
    }

    public void setStrikeThruText(boolean strikeThruText) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setStrikeThruText", "%b", strikeThruText);
    }

    public void setUnderlineText(boolean underlineText) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setUnderlineText", "%b", underlineText);
    }

    public void setTypeface(Typeface typeface) {
        checkInheritance();
        OutputBuilder.addImport(Typeface.class);
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, typeface), "setTypeface", "%s", typeface.getInstanceName(this));
    }

    public void setColor(int color) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setColor", "%s", ColorReplacements.findColorReplacement(color));
    }

    public void getTextPath(String text, int start, int end, float x, float y, Path path) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, path), "getTextPath", "%s, %d, %d, %ff, %ff, %s", text, start, end, x, y, path.getInstanceName(this));
    }

    @Override
    public String getInstanceName(AndroidClass referringClass) {
        //If this instance had a parent instance then that instance must receive the referring class
        if (mParent != null && referringClass != null) {
            return mParent.getInstanceName(referringClass);
        }

        return super.getInstanceName(referringClass);
    }

    private void checkInheritance() {
        //This function is called when a property is set from the outside.
        //If the instance is "inherited", that means it was not initialized and represents the same
        //Paint instance which was used in the constructor as parameter.
        //But since this instance will be changed now we need a real new Paint instance, so we create it now.
        if (mParent != null) {
            //This instance is referring to the parent class, must be initialized from that
            init(OutputBuilder.dependencyList(mParent));
            OutputBuilder.append(this, this, OutputBuilder.dependencyList(mParent), "%s.set(%s);", getInstanceName(null), mParent.getInstanceName(this));
            mParent = null;
        }
    }

    public enum Style {
        STROKE, FILL
    }

    public enum Cap {ROUND, SQUARE, BUTT}

    public enum Join {ROUND, BEVEL, MITER}
}
