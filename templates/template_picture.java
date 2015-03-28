package %s;

%s
import android.graphics.Picture;

public class %s {
  public static Picture renderToPicture(int width, int height) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(width, height);
        canvas.scale(width, height);

%s
        picture.endRecording();
        return picture;
  }
}