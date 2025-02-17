package org.mliboot.mlspd.service;

interface IRemotePreferenceCallback {
    oneway void onUpdate(in Bundle map);
}
