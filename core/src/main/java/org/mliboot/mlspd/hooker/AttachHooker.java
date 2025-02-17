package org.mliboot.mlspd.hooker;

import android.app.ActivityThread;

import de.robv.android.xposed.XposedInit;
import org.mliboot.libxp.api.IXpInter;
import org.mliboot.libxp.api.annotations.AfterInvocation;
import org.mliboot.libxp.api.annotations.XpHandler;

@XpHandler
public class AttachHooker implements IXpInter.Hooker {

    @AfterInvocation
    public static void afterHookedMethod(IXpInter.AfterHookCallback callback) {
        XposedInit.loadModules((ActivityThread) callback.getThisObject());
    }
}
