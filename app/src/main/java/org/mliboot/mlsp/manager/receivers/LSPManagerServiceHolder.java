

package org.mliboot.mlsp.manager.receivers;

import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.system.Os;

import org.mliboot.mlspd.ILSPManagerService;

public class LSPManagerServiceHolder implements IBinder.DeathRecipient {
    private static LSPManagerServiceHolder holder = null;
    private static ILSPManagerService service = null;

    public static void init(IBinder binder) {
        if (holder == null) {
            holder = new LSPManagerServiceHolder(binder);
        }
    }

    public static ILSPManagerService getService() {
        return service;
    }

    private LSPManagerServiceHolder(IBinder binder) {
        linkToDeath(binder);
        service = ILSPManagerService.Stub.asInterface(binder);
    }

    private void linkToDeath(IBinder binder) {
        try {
            binder.linkToDeath(this, 0);
        } catch (RemoteException e) {
            binderDied();
        }
    }

    @Override
    public void binderDied() {
        System.exit(0);
        Process.killProcess(Os.getpid());
    }
}
