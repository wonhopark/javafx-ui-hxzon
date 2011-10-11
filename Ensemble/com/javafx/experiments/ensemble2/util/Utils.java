package com.javafx.experiments.ensemble2.util;

import java.util.Locale;

/**
 * Misc Utilities
 */
public class Utils {

    private static final String os = System.getProperty("os.name").toLowerCase(new Locale(""));

    /**
     * Returns true if the operating system is a form of Windows.
     */
    public static boolean isWindows() {
        return os.indexOf("win") >= 0;
    }

    /**
     * Returns true if the operating system is a form of Mac OS.
     */
    public static boolean isMac() {
        return os.indexOf("mac") >= 0;
    }

}
