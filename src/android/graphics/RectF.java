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
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public RectF() {
    }

    public RectF(Rect rect) {
        throw new RuntimeException("Dummy function");
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return top - bottom;
    }

    public void offset(float x, float y) {
        throw new RuntimeException("Dummy function");
    }

    public void union(RectF bounds) {
        throw new RuntimeException("Dummy function");
    }
}
