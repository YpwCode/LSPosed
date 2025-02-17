package org.mliboot.mlspd.service;

import org.mliboot.mlspd.service.ILSPApplicationService;

interface ILSPosedService {
    ILSPApplicationService requestApplicationService(int uid, int pid, String processName, IBinder heartBeat);

    oneway void dispatchSystemServerContext(in IBinder activityThread, in IBinder activityToken, String api);

    boolean preStartManager(String pkgName, in Intent intent);
}
