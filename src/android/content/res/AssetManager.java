package android.content.res;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by racs on 2015.03.17..
 */
public class AssetManager {
    public InputStream open(String filename) throws IOException {
        // Try to open the file from current directory
        return new FileInputStream(filename);
    }
}
