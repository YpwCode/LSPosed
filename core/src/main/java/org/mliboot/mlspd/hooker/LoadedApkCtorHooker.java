package org.mliboot.mlspd.hooker;

import android.app.LoadedApk;
import android.content.res.XResources;
import android.util.Log;

import org.mliboot.mlspd.util.Hookers;

import org.md.xlib.XposedHelpers;
import org.md.xlib.XposedInit;
import org.mliboot.libxp.api.IXpInter;
import org.mliboot.libxp.api.annotations.AfterInvocation;
import org.mliboot.libxp.api.annotations.XpHandler;

// when a package is loaded for an existing process, trigger the callbacks as well
@XpHandler
public class LoadedApkCtorHooker implements IXpInter.Hooker {

    @AfterInvocation
    public static void afterHookedMethod(IXpInter.AfterHookCallback callback) {
        Hookers.logD("LoadedApk#<init> starts");

        try {
            LoadedApk loadedApk = (LoadedApk) callback.getThisObject();
            assert loadedApk != null;
            String packageName = loadedApk.getPackageName();
            Object mAppDir = XposedHelpers.getObjectField(loadedApk, "mAppDir");
            Hookers.logD("LoadedApk#<init> ends: " + mAppDir);

            if (!XposedInit.disableResources) {
                XResources.setPackageNameForResDir(packageName, loadedApk.getResDir());
            }

            if (packageName.equals("android")) {
                if (XposedInit.startsSystemServer) {
                    Hookers.logD("LoadedApk#<init> is android, skip: " + mAppDir);
                    return;
                } else {
                    packageName = "system";
                }
            }

            if (!XposedInit.loadedPackagesInProcess.add(packageName)) {
                Hookers.logD("LoadedApk#<init> has been loaded before, skip: " + mAppDir);
                return;
            }

            // OnePlus magic...
            if (Log.getStackTraceString(new Throwable()).
                    contains("android.app.ActivityThread$ApplicationThread.schedulePreload")) {
                Hookers.logD("LoadedApk#<init> maybe oneplus's custom opt, skip");
                return;
            }

            LoadedApkCreateCLHooker.addLoadedApk(loadedApk);
        } catch (Throwable t) {
            Hookers.logE("error when hooking LoadedApk.<init>", t);
        }
    }
}
