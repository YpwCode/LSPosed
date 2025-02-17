

package org.mliboot.mlspd.core;

import android.os.IBinder;
import android.os.Process;

import org.mliboot.mlspd.service.ILSPApplicationService;
import org.mliboot.mlspd.util.ParasiticManagerHooker;
import org.mliboot.mlspd.util.Utils;
import org.mliboot.mlspd.BuildConfig;

public class Main {

    public static void forkCommon(boolean isSystem, String niceName, String appDir, IBinder binder) {
        Utils.logI("isSystem:"+isSystem+", niceName:"+niceName+", appDir:"+appDir+", binder:"+binder.toString());
        Startup.initXposed(isSystem, niceName, appDir, ILSPApplicationService.Stub.asInterface(binder));
        if ((niceName.equals(BuildConfig.MANAGER_INJECTED_PKG_NAME) || niceName.equals(BuildConfig.DEFAULT_MANAGER_PACKAGE_NAME))
                && ParasiticManagerHooker.start()) {
            Utils.logI("Loaded manager, skipping next steps");
            return;
        }
        Utils.logI("Loading xposed for " + niceName + "/" + Process.myUid());
        Startup.bootstrapXposed();
    }
}
