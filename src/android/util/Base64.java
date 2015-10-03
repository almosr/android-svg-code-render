package android.util;

/**
 * Created by racs on 2015.03.17..
 */
public class Base64 {
    // This flag is ignored, required only for source level compatibility
    public static final int DEFAULT = 0;

    public static byte[] decode(String base64string, int flags) {
        return java.util.Base64.getDecoder().decode(base64string);
    }
}
