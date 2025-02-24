package org.mliboot.mlspd.hooker;

import static org.mliboot.mlspd.util.Utils.logD;

import androidx.annotation.NonNull;

import org.mliboot.mlspd.impl.LSPosedContext;
import org.mliboot.mlspd.util.Hookers;

import org.md.xlib.XposedBridge;
import org.md.xlib.XposedInit;
import org.md.xlib.callbacks.XC_LoadPackage;
import org.mliboot.libxp.api.IXpInter;
import org.mliboot.libxp.api.IXpModuleInter;
import org.mliboot.libxp.api.annotations.BeforeInvocation;
import org.mliboot.libxp.api.annotations.XpHandler;

@XpHandler
public class StartBootstrapServicesHooker implements IXpInter.Hooker {

    @BeforeInvocation
    public static void beforeHookedMethod() {
        logD("SystemServer#startBootstrapServices() starts");

        try {
            XposedInit.loadedPackagesInProcess.add("android");

            XC_LoadPackage.LoadPackageParam lpparam = new XC_LoadPackage.LoadPackageParam(XposedBridge.sLoadedPackageCallbacks);
            lpparam.packageName = "android";
            lpparam.processName = "android"; // it's actually system_server, but other functions return this as well
            lpparam.classLoader = HandleSystemServerProcessHooker.systemServerCL;
            lpparam.appInfo = null;
            lpparam.isFirstApplication = true;
            XC_LoadPackage.callAll(lpparam);

            LSPosedContext.callOnSystemServerLoaded(new IXpModuleInter.SystemServerLoadedParam() {
                @Override
                @NonNull
                public ClassLoader getClassLoader() {
                    return HandleSystemServerProcessHooker.systemServerCL;
                }
            });
        } catch (Throwable t) {
            Hookers.logE("error when hooking startBootstrapServices", t);
        }
    }
}
