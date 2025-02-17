package org.mliboot.mlspd.service;

import org.mliboot.mlspd.service.IRemotePreferenceCallback;

interface ILSPInjectedModuleService {
    int getFrameworkPrivilege();

    Bundle requestRemotePreferences(String group, IRemotePreferenceCallback callback);

    ParcelFileDescriptor openRemoteFile(String path);

    String[] getRemoteFileList();
}
