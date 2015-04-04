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
        init(null);
    }

    public void transform(Matrix currentMatrix, Path transformedPath) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, currentMatrix, transformedPath), "transform", "%s, %s", currentMatrix.getInstanceName(this), transformedPath.getInstanceName(this));
    }

    public Path.FillType getFillType() {
        return mFillType;
    }

    public void setFillType(Path.FillType fillType) {
        mFillType = fillType;
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "setFillType", "Path.FillType.%s", fillType.name());
    }

    public void computeBounds(RectF bounds, boolean exact) {
        //TODO: simulate computeBound in Path class
    }

    public void transform(Matrix transform) {
        throw new RuntimeException("Dummy function");
    }

    public void moveTo(float x, float y) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "moveTo", "%ff, %ff", x, y);
    }

    public void lineTo(float x, float y) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "lineTo", "%ff, %ff", x, y);
    }

    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "cubicTo", "%ff, %ff, %ff, %ff, %ff, %ff", x1, y1, x2, y2, x3, y3);
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "quadTo", "%ff, %ff, %ff, %ff", x1, y1, x2, y2);
    }

    public void close() {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this), "close");
    }

    public void addPath(Path path, Matrix matrix) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, path, matrix), "addPath", "%s, %s", path.getInstanceName(this), matrix.getInstanceName(this));
    }

    public void addPath(Path src) {
        OutputBuilder.appendMethodCall(this, OutputBuilder.dependencyList(this, src), "addPath", "%s", src.getInstanceName(this));
    }

    public enum FillType {EVEN_ODD, WINDING}
}
