package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class Paint {
    public static final int ANTI_ALIAS_FLAG = 1;
    public static final int DEV_KERN_TEXT_FLAG = 2;
    public static final int SUBPIXEL_TEXT_FLAG = 3;
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

    public Paint(Paint fillPaint) {
        throw new RuntimeException("Dummy function");
    }

    public Paint() {
    }

    public void setFlags(int flags) {
        throw new RuntimeException("Dummy function");
    }

    public void setStyle(Style style) {
        throw new RuntimeException("Dummy function");
    }

    public void setTypeface(int typeface) {
        throw new RuntimeException("Dummy function");
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public Shader getShader() {
        return mShader;
    }

    public void setShader(Shader shader) {
        mShader = shader;
    }

    public float measureText(String text) {
        throw new RuntimeException("Dummy function");
    }

    public void getTextBounds(String text, int start, int count, Rect rect) {
        throw new RuntimeException("Dummy function");
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    public void setStrokeCap(Cap strokeCap) {
        mStrokeCap = strokeCap;
    }

    public void setStrokeJoin(Join strokeJoin) {
        mStrokeJoin = strokeJoin;
    }

    public Float getStrokeMiter() {
        return mStrokeMiter;
    }

    public void setStrokeMiter(Float strokeMiter) {
        mStrokeMiter = strokeMiter;
    }

    public PathEffect getPathEffect() {
        return mPathEffect;
    }

    public void setPathEffect(PathEffect pathEffect) {
        mPathEffect = pathEffect;
    }

    public boolean isStrikeThruText() {
        return mStrikeThruText;
    }

    public void setStrikeThruText(boolean strikeThruText) {
        mStrikeThruText = strikeThruText;
    }

    public boolean isUnderlineText() {
        return mUnderlineText;
    }

    public void setUnderlineText(boolean underlineText) {
        mUnderlineText = underlineText;
    }

    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void getTextPath(String text, int start, int end, float x, float y, Path path) {
        throw new RuntimeException("Dummy function");
    }

    public enum Style {
        STROKE, FILL
    }

    public enum Cap {ROUND, SQUARE, BUTT}

    public enum Join {ROUND, BEVEL, MITER}
}
