package android.graphics;

/**
 * Created by racs on 2015.03.17..
 */
public class RectF {
    public float left;
    public float top;
    public float right;
    public float bottom;

    public RectF(float left, float top, float right, float bottom) {
        initFields(left, top, right, bottom);
    }

    public RectF() {
    }

    public RectF(Rect rect) {
        //TODO: RectF constructor with Rect parameter used only by text rendering, not supported yet
    }

    private void initFields(float left, float top, float right, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return top - bottom;
    }

    public void offset(float x, float y) {
        left += x;
        right += x;
        top += y;
        bottom += y;
    }

    public void union(RectF bounds) {
        union(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public void union(float left, float top, float right, float bottom) {
        //Direct copy from Android sources: http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.2_r1/android/graphics/Rect.java#Rect.union%28int%2Cint%2Cint%2Cint%29
        if ((left < right) && (top < bottom)) {
            if ((this.left < this.right) && (this.top < this.bottom)) {
                if (this.left > left)
                    this.left = left;
                if (this.top > top)
                    this.top = top;
                if (this.right < right)
                    this.right = right;
                if (this.bottom < bottom)
                    this.bottom = bottom;
            } else {
                this.left = left;
                this.top = top;
                this.right = right;
                this.bottom = bottom;
            }
        }
    }
}
