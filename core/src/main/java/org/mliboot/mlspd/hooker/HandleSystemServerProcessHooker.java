

package org.mliboot.mlspd.hooker;

import android.annotation.SuppressLint;

import org.mliboot.mlspd.deopt.PrebuiltMethodsDeopter;
import org.mliboot.mlspd.impl.LSPosedHelper;
import org.mliboot.mlspd.util.Hookers;

import org.mliboot.libxp.api.IXpInter;
import org.mliboot.libxp.api.annotations.AfterInvocation;
import org.mliboot.libxp.api.annotations.XpHandler;

// system_server initialization
@XpHandler
public class HandleSystemServerProcessHooker implements IXpInter.Hooker {

    public static volatile ClassLoader systemServerCL;

    @SuppressLint("PrivateApi")
    @AfterInvocation
    public static void afterHookedMethod() {
        Hookers.logD("ZygoteInit#handleSystemServerProcess() starts");
        try {
            // get system_server classLoader
            systemServerCL = Thread.currentThread().getContextClassLoader();
            // deopt methods in SYSTEMSERVERCLASSPATH
            PrebuiltMethodsDeopter.deoptSystemServerMethods(systemServerCL);
            var clazz = Class.forName("com.android.server.SystemServer", false, systemServerCL);
            LSPosedHelper.hookAllMethods(StartBootstrapServicesHooker.class, clazz, "startBootstrapServices");
        } catch (Throwable t) {
            Hookers.logE("error when hooking systemMain", t);
        }
    }
}
