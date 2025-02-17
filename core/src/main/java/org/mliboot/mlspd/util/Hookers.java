

package org.mliboot.mlspd.util;

import android.app.ActivityThread;

public class Hookers {

    public static void logD(String prefix) {
        Utils.logD(String.format("%s: pkg=%s, prc=%s", prefix, ActivityThread.currentPackageName(),
            ActivityThread.currentProcessName()));
    }

    public static void logE(String prefix, Throwable throwable) {
        Utils.logE(String.format("%s: pkg=%s, prc=%s", prefix, ActivityThread.currentPackageName(),
            ActivityThread.currentProcessName()), throwable);
    }

}
