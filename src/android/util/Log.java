package android.util;

/**
 * Created by racs on 2015.03.17..
 */
public class Log {
    public static void e(String tag, String s) {
        printLog("E", tag, s);
    }

    public static void w(String tag, String s) {
        printLog("W", tag, s);
    }

    public static void i(String tag, String s) {
        printLog("I", tag, s);
    }

    public static void d(String tag, String s) {
        printLog("D", tag, s);
    }

    private static void printLog(String level, String tag, String s) {
        System.out.println(String.format("%s/%s: %s", level, tag, s));
    }
}
