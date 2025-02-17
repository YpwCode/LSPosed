package org.mliboot.mlspd.models;
import org.mliboot.mlspd.models.PreLoadedApk;
import org.mliboot.mlspd.service.ILSPInjectedModuleService;

parcelable Module {
    String packageName;
    int appId;
    String apkPath;
    PreLoadedApk file;
    ApplicationInfo applicationInfo;
    ILSPInjectedModuleService service;
}
