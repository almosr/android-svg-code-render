package android.graphics;

public class PorterDuffXfermode extends Xfermode {
    public PorterDuffXfermode(PorterDuff.Mode mode) {
        init("%s", String.format("PorterDuff.Mode.%s", mode.name()));
    }
}