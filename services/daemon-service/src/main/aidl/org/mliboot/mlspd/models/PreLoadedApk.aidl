package org.mliboot.mlspd.models;

parcelable PreLoadedApk {
    List<SharedMemory> preLoadedDexes;
    List<String> moduleClassNames;
    List<String> moduleLibraryNames;
    boolean legacy;
}
