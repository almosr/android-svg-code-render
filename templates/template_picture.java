package %s;

%s
import android.graphics.Picture;

public class %s {

%s
    private boolean inited;
%s
    public void init() {
        if (inited) return;
        inited = true;
%s
    }

    public Picture renderToPicture(int width, int height) {
        init();
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(width, height);
        canvas.scale(width / WIDTH, height / HEIGHT);

%s
        picture.endRecording();
        return picture;
    }
}