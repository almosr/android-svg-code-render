package %s;

%s
import android.graphics.Picture;

public class %s {

%s
    public static Picture renderToPicture(int width, int height) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(width, height);
        canvas.scale(width / WIDTH, height / HEIGHT);

%s
        picture.endRecording();
        return picture;
    }
}