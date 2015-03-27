package android.graphics;

import android_svg_code_render.AndroidClass;
import android_svg_code_render.OutputBuilder;

/**
 * Simulated Android Path class
 *
 * @author Almos Rajnai
 */
public class Path extends AndroidClass {

    private Path.FillType mFillType;

    public Path() {
        init();
    }

    public void transform(Matrix currentMatrix, Path transformedPath) {
        OutputBuilder.appendMethodCall(this, "transform", "%s, %s", currentMatrix.getInstanceName(this), transformedPath.getInstanceName(this));
    }

    public Path.FillType getFillType() {
        return mFillType;
    }

    public void setFillType(Path.FillType fillType) {
        mFillType = fillType;
        OutputBuilder.appendMethodCall(this, "setFillType", "Path.FillType.%s", fillType.name());
    }

    public void computeBounds(RectF bounds, boolean exact) {
        //TODO: simulate computeBound in Path class
    }

    public void transform(Matrix transform) {
        throw new RuntimeException("Dummy function");
    }

    public void moveTo(float x, float y) {
        OutputBuilder.appendMethodCall(this, "moveTo", "%ff, %ff", x, y);
    }

    public void lineTo(float x, float y) {
        OutputBuilder.appendMethodCall(this, "lineTo", "%ff, %ff", x, y);
    }

    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        OutputBuilder.appendMethodCall(this, "cubicTo", "%ff, %ff, %ff, %ff, %ff, %ff", x1, y1, x2, y2, x3, y3);
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        OutputBuilder.appendMethodCall(this, "quadTo", "%ff, %ff, %ff, %ff", x1, y1, x2, y2);
    }

    public void close() {
        OutputBuilder.appendMethodCall(this, "close");
    }

    public void addPath(Path path, Matrix matrix) {
        OutputBuilder.appendMethodCall(this, "addPath", "%s, %s", path.getInstanceName(this), matrix.getInstanceName(this));
    }

    public void addPath(Path src) {
        OutputBuilder.appendMethodCall(this, "addPath", "%s", src.getInstanceName(this));
    }

    public enum FillType {EVEN_ODD, WINDING}
}
