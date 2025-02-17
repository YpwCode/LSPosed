

package org.mliboot.mlspd.util;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRulesException;

public class Utils {

    public static final String LOG_TAG = "Ypw";
    public static final boolean isMIUI = !TextUtils.isEmpty(SystemProperties.get("ro.miui.ui.version.name"));
    public static final boolean isLENOVO = !TextUtils.isEmpty(SystemProperties.get("ro.lenovo.region"));

    public static void logD(Object msg) {
        Log.d(LOG_TAG, msg.toString());
    }

    public static void logD(String msg, Throwable throwable) {
        Log.d(LOG_TAG, msg, throwable);
    }

    public static void logW(String msg) {
        Log.w(LOG_TAG, msg);
    }

    public static void logW(String msg, Throwable throwable) {
        Log.w(LOG_TAG, msg, throwable);
    }

    public static void logI(String msg) {
        Log.i(LOG_TAG, msg);
    }

    public static void logI(String msg, Throwable throwable) {
        Log.i(LOG_TAG, msg, throwable);
    }

    public static void logE(String msg) {
        Log.e(LOG_TAG, msg);
    }

    public static void logE(String msg, Throwable throwable) {
        Log.e(LOG_TAG, msg, throwable);
    }

    public static ZoneId getZoneId() {
        var timezone = SystemProperties.get("persist.sys.timezone", "GMT");
        try {
            return ZoneId.of(timezone);
        } catch (ZoneRulesException e) {
            return ZoneOffset.UTC;
        }
    }
}
