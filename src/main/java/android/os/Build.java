package android.os;

import android_svg_code_render.AndroidVersionSimulation;

/**
 * Created by racs on 2015.03.17..
 */
public class Build {
    public static class VERSION_CODES {
        public static final int KITKAT = 19;
    }

    //Here the OS would report the current Android API version.
    //But this is just a simulation, so we are going to trick the androidsvg library
    //to accept the minimum version which was set as a parameter to the processing.
    public class VERSION extends AndroidVersionSimulation {}
}