

package org.mliboot.mlspd.core;

import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import org.mliboot.mlspd.models.Module;
import org.mliboot.mlspd.service.ILSPApplicationService;
import org.mliboot.mlspd.util.Utils;

import java.util.Collections;
import java.util.List;

public class ApplicationServiceClient implements ILSPApplicationService, IBinder.DeathRecipient {
    public static ApplicationServiceClient serviceClient = null;

    final ILSPApplicationService service;

    final String processName;

    private ApplicationServiceClient(@NonNull ILSPApplicationService service, @NonNull String processName) throws RemoteException {
        this.service = service;
        this.processName = processName;
        this.service.asBinder().linkToDeath(this, 0);
    }

    synchronized static void Init(ILSPApplicationService service, String niceName) {
        var binder = service.asBinder();
        if (serviceClient == null && binder != null) {
            try {
                serviceClient = new ApplicationServiceClient(service, niceName);
            } catch (RemoteException e) {
                Utils.logE("link to death error: ", e);
            }
        }
    }

    @Override
    public List<Module> getLegacyModulesList() {
        try {
            return service.getLegacyModulesList();
        } catch (RemoteException | NullPointerException ignored) {
        }
        return Collections.emptyList();
    }

    @Override
    public List<Module> getModulesList() {
        try {
            return service.getModulesList();
        } catch (RemoteException | NullPointerException ignored) {
        }
        return Collections.emptyList();
    }

    @Override
    public String getPrefsPath(String packageName) {
        try {
            return service.getPrefsPath(packageName);
        } catch (RemoteException | NullPointerException ignored) {
        }
        return null;
    }

    @Override
    public ParcelFileDescriptor requestInjectedManagerBinder(List<IBinder> binder) {
        try {
            return service.requestInjectedManagerBinder(binder);
        } catch (RemoteException | NullPointerException ignored) {
        }
        return null;
    }

    @Override
    public IBinder asBinder() {
        return service.asBinder();
    }

    @Override
    public void binderDied() {
        service.asBinder().unlinkToDeath(this, 0);
        serviceClient = null;
    }
}
