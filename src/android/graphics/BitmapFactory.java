package android.graphics;

import android.util.ByteConstantArray;
import android_svg_code_render.OutputBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by racs on 2015.03.17..
 */
public class BitmapFactory {
    public static Bitmap decodeStream(InputStream inputStream) {
        //Read the file into a byte array and handle it as embedded bitmap
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            byte[] byteArray = buffer.toByteArray();
            return decodeByteArray(byteArray, 0, byteArray.length);
        } catch (IOException e) {
            throw new RuntimeException("Error while decode bitmap input stream", e);
        }
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        try {
            //Try to decode the image to find out bitmap dimensions
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));

            ByteConstantArray byteArrayConstant = new ByteConstantArray(data);
            Bitmap bitmap = new Bitmap(img.getWidth(), img.getHeight());
            OutputBuilder.append(bitmap, "Bitmap %s = BitmapFactory.decodeByteArray(%s, %d, %d);", bitmap.getInstanceName(null), byteArrayConstant.getInstanceName(bitmap), offset, length);
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException("Unable to decode embedded or linked bitmap", e);
        }
    }
}
