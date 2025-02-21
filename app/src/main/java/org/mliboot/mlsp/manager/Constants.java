package org.mliboot.mlsp.manager;

import android.os.IBinder;
import android.util.Log;

import org.mliboot.mlsp.manager.receivers.LSPManagerServiceHolder;

public class Constants {
    public static boolean setBinder(IBinder binder) {
        Log.d("TAG", Log.getStackTraceString(new Throwable()));
        LSPManagerServiceHolder.init(binder);
        return LSPManagerServiceHolder.getService().asBinder().isBinderAlive();
    }
}
