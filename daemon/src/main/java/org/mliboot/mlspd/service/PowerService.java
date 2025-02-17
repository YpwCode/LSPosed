

package org.mliboot.mlspd.service;

import static android.content.Context.POWER_SERVICE;
import static org.mliboot.mlspd.service.ServiceManager.TAG;

import android.os.IBinder;
import android.os.IPowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class PowerService {
    private static IPowerManager pm = null;
    private static IBinder binder = null;
    private static final IBinder.DeathRecipient recipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w(TAG, "PowerManager is dead");
            binder.unlinkToDeath(this, 0);
            binder = null;
            pm = null;
        }
    };

    private static IPowerManager getPowerManager() {
        if (binder == null || pm == null) {
            binder = ServiceManager.getService(POWER_SERVICE);
            if (binder == null) return null;
            try {
                binder.linkToDeath(recipient, 0);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            pm = IPowerManager.Stub.asInterface(binder);
        }
        return pm;
    }

    public static void reboot(boolean confirm, String reason, boolean wait) throws RemoteException {
        IPowerManager pm = getPowerManager();
        if (pm == null) return;
        pm.reboot(confirm, reason, wait);
    }
}
