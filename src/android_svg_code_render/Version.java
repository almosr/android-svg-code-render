package android_svg_code_render;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Version number and build date handler class
 *
 * @author Almos Rajnai
 */
public class Version {
    public static final int MAJOR = 1;
    public static final int MINOR = 2;
    public static final int REVISION = 0;

    public static final String FULL = String.format("%d.%d.%d", MAJOR, MINOR, REVISION);

    public static final Date BUILD_TIME = getClassBuildTime();

    /**
     * Handles files, jar entries, and deployed jar entries in a zip file (EAR).
     * Copied from this page: http://stackoverflow.com/a/22404140/602549
     *
     * @return The date if it can be determined, or null if not.
     */
    private static Date getClassBuildTime() {
        Date d = null;
        Class<?> currentClass = new Object() {
        }.getClass().getEnclosingClass();
        URL resource = currentClass.getResource(currentClass.getSimpleName() + ".class");
        if (resource != null) {
            switch (resource.getProtocol()) {
                case "file":
                    try {
                        d = new Date(new File(resource.toURI()).lastModified());
                    } catch (URISyntaxException ignored) {
                    }
                    break;

                case "jar": {
                    String path = resource.getPath();
                    d = new Date(new File(path.substring(5, path.indexOf("!"))).lastModified());
                    break;
                }

                case "zip": {
                    String path = resource.getPath();
                    File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));
                    try (JarFile jf = new JarFile(jarFileOnDisk)) {
                        ZipEntry ze = jf.getEntry(path.substring(path.indexOf("!") + 2));//Skip the ! and the /
                        d = new Date(ze.getTime());
                    } catch (IOException | RuntimeException ignored) {
                    }
                    break;
                }
            }
        }

        return d;
    }
}
