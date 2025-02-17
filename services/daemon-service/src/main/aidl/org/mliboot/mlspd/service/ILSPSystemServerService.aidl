package org.mliboot.mlspd.service;

import org.mliboot.mlspd.service.ILSPApplicationService;

interface ILSPSystemServerService {
    ILSPApplicationService requestApplicationService(int uid, int pid, String processName, IBinder heartBeat);
}
