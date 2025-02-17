

package org.mliboot.mlspd.deopt;

import static org.mliboot.mlspd.deopt.InlinedMethodCallers.KEY_BOOT_IMAGE;
import static org.mliboot.mlspd.deopt.InlinedMethodCallers.KEY_BOOT_IMAGE_MIUI_RES;
import static org.mliboot.mlspd.deopt.InlinedMethodCallers.KEY_SYSTEM_SERVER;

import org.mliboot.mlspd.nativebridge.HookBridge;
import org.mliboot.mlspd.util.Hookers;
import org.mliboot.mlspd.util.Utils;

import java.lang.reflect.Executable;
import java.util.Arrays;

import de.robv.android.xposed.XposedHelpers;

public class PrebuiltMethodsDeopter {

    public static void deoptMethods(String where, ClassLoader cl) {
        Object[][] callers = InlinedMethodCallers.get(where);
        if (callers == null) {
            return;
        }
        for (Object[] caller : callers) {
            try {
                if (caller.length < 2) continue;
                if (!(caller[0] instanceof String)) continue;
                if (!(caller[1] instanceof String)) continue;
                Executable method;
                Object[] params = new Object[caller.length - 2];
                System.arraycopy(caller, 2, params, 0, params.length);
                if ("<init>".equals(caller[1])) {
                    method = XposedHelpers.findConstructorExactIfExists((String) caller[0], cl, params);
                } else {
                    method = XposedHelpers.findMethodExactIfExists((String) caller[0], cl, (String) caller[1], params);
                }
                if (method != null) {
                    Hookers.logD("deoptimizing " + method);
                    HookBridge.deoptimizeMethod(method);
                }
            } catch (Throwable throwable) {
                Utils.logE("error when deopting method: " + Arrays.toString(caller), throwable);
            }
        }
    }

    public static void deoptBootMethods() {
        // todo check if has been done before
        deoptMethods(KEY_BOOT_IMAGE, null);
    }

    public static void deoptResourceMethods() {
        if (Utils.isMIUI) {
            //deopt these only for MIUI
            deoptMethods(KEY_BOOT_IMAGE_MIUI_RES, null);
        }
    }

    public static void deoptSystemServerMethods(ClassLoader sysCL) {
        deoptMethods(KEY_SYSTEM_SERVER, sysCL);
    }
}
