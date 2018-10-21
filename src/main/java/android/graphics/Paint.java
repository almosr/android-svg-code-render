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
    public static final int FILTER_BITMAP_FLAG = 2;

    public static final int DEV_KERN_TEXT_FLAG = 2;
    public static final int SUBPIXEL_TEXT_FLAG = 4;
    private static final int[] FLAG_VALUES = {ANTI_ALIAS_FLAG, DEV_KERN_TEXT_FLAG, SUBPIXEL_TEXT_FLAG};
    private static final String[] FLAG_NAMES = {"ANTI_ALIAS_FLAG", "DEV_KERN_TEXT_FLAG", "SUBPIXEL_TEXT_FLAG"};

    private float mTextSize;
    private Shader mShader;

    public Paint(int flags) {
        String flagString = OutputBuilder.splitFlags(flags, "Paint.", FLAG_VALUES, FLAG_NAMES);
        init("%s", flagString);
    }

    public Paint(Paint paint) {
        setParent(paint);
    }

    public Paint() {
        init();
    }

    public void setFlags(int flags) {
        checkInheritance();
        String flagString = OutputBuilder.splitFlags(flags, "Paint.", FLAG_VALUES, FLAG_NAMES);
        OutputBuilder.appendMethodCall(this, "setFlags", "%s", flagString);
    }

    public void setStyle(Style style) {
        checkInheritance();
        OutputBuilder.addImport(Paint.class);
        OutputBuilder.appendMethodCall(this, "setStyle", "Paint.Style.%s", style.name());
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        checkInheritance();
        mTextSize = textSize;
        OutputBuilder.appendMethodCall(this, "setTextSize", "%ff", textSize);
    }

    public void setTextAlign(Align textAlign) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "setTextAlign", "Paint.Align.%s", textAlign.name());
    }

    public Shader getShader() {
        return mShader;
    }

    public void setShader(Shader shader) {
        checkInheritance();
        mShader = shader;
        OutputBuilder.appendMethodCall(this, "setShader", "%s", shader.getInstanceName(this));
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
        OutputBuilder.appendMethodCall(this, "setStrokeWidth", "%ff", strokeWidth);
    }

    public void setStrokeCap(Cap strokeCap) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "setStrokeCap", "Paint.Cap.%s", strokeCap.name());
    }

    public void setStrokeJoin(Join strokeJoin) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "setStrokeJoin", "Paint.Join.%s", strokeJoin.name());
    }

    public void setStrokeMiter(Float strokeMiter) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "setStrokeMiter", "%ff", strokeMiter);
    }

    public void setPathEffect(PathEffect pathEffect) {
        checkInheritance();
        if (pathEffect != null) {
            OutputBuilder.addImport(pathEffect.getClass());
            OutputBuilder.appendMethodCall(this, "setPathEffect", "%s", pathEffect.getClass().getSimpleName());
        } else {
            OutputBuilder.appendMethodCall(this, "setPathEffect", "null");
        }
    }

    public void setStrikeThruText(boolean strikeThruText) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "setStrikeThruText", "%b", strikeThruText);
    }

    public void setUnderlineText(boolean underlineText) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "setUnderlineText", "%b", underlineText);
    }

    public void setTypeface(Typeface typeface) {
        checkInheritance();
        OutputBuilder.addImport(Typeface.class);
        OutputBuilder.appendMethodCall(this, "setTypeface", "%s", typeface.getInstanceName(this));
    }

    public void setColor(int color) {
        checkInheritance();
        OutputBuilder.appendMethodCall(this, "setColor", "%s", ColorReplacements.findColorReplacement(color));
    }

    public void getTextPath(String text, int start, int end, float x, float y, Path path) {
        OutputBuilder.appendMethodCall(this, "getTextPath", "%s, %d, %d, %ff, %ff, %s", text, start, end, x, y, path.getInstanceName(this));
    }

    @Override
    public String getInstanceName(AndroidClass referringClass) {
        //If this instance had a parent instance then that instance must receive the referring class
        if (mParent != null && referringClass != null) {
            return mParent.getInstanceName(referringClass);
        }

        return super.getInstanceName(referringClass);
    }

    public void setColorFilter(ColorFilter filter) {
        OutputBuilder.appendMethodCall(this, "setColorFilter", "%s", filter.getInstanceName(this));
    }

    public void setXfermode(Xfermode xfermode) {
        OutputBuilder.appendMethodCall(this, "setXfermode", "%s", xfermode.getInstanceName(this));
    }

    public void setAlpha(int alpha) {
        OutputBuilder.appendMethodCall(this, "setAlpha", "%d", alpha);
    }

    public enum Style {STROKE, FILL}

    public enum Cap {ROUND, SQUARE, BUTT}

    public enum Join {ROUND, BEVEL, MITER}

    public enum Align {LEFT, CENTER, RIGHT}
}
