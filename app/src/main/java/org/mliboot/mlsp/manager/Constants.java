

package org.mliboot.mlsp.manager;

import android.os.IBinder;

import org.mliboot.mlsp.manager.receivers.LSPManagerServiceHolder;

public class Constants {
    public static boolean setBinder(IBinder binder) {
        LSPManagerServiceHolder.init(binder);
        return LSPManagerServiceHolder.getService().asBinder().isBinderAlive();
    }
}
