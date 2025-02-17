package org.mliboot.mlspd.hooker;

import android.util.Log;

import org.mliboot.mlspd.impl.LSPosedBridge;

import org.mliboot.libxp.api.IXpInter;
import org.mliboot.libxp.api.annotations.BeforeInvocation;
import org.mliboot.libxp.api.annotations.XpHandler;

@XpHandler
public class CrashDumpHooker implements IXpInter.Hooker {

    @BeforeInvocation
    public static void beforeHookedMethod(IXpInter.BeforeHookCallback callback) {
        try {
            var e = (Throwable) callback.getArgs()[0];
            LSPosedBridge.log("Crash unexpectedly: " + Log.getStackTraceString(e));
        } catch (Throwable ignored) {
        }
    }
}
