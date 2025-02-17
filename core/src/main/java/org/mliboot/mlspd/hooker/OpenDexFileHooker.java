package org.mliboot.mlspd.hooker;

import android.os.Build;

import org.mliboot.mlspd.impl.LSPosedBridge;
import org.mliboot.mlspd.nativebridge.HookBridge;

import org.mliboot.libxp.api.IXpInter;
import org.mliboot.libxp.api.annotations.AfterInvocation;
import org.mliboot.libxp.api.annotations.XpHandler;

@XpHandler
public class OpenDexFileHooker implements IXpInter.Hooker {

    @AfterInvocation
    public static void afterHookedMethod(IXpInter.AfterHookCallback callback) {
        ClassLoader classLoader = null;
        for (var arg : callback.getArgs()) {
            if (arg instanceof ClassLoader) {
                classLoader = (ClassLoader) arg;
            }
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P && classLoader == null) {
            classLoader = LSPosedBridge.class.getClassLoader();
        }
        while (classLoader != null) {
            if (classLoader == LSPosedBridge.class.getClassLoader()) {
                HookBridge.setTrusted(callback.getResult());
                return;
            } else {
                classLoader = classLoader.getParent();
            }
        }
    }
}
