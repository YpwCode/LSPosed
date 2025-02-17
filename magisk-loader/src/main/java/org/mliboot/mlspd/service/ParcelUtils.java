

package org.mliboot.mlspd.service;

import android.annotation.SuppressLint;
import android.os.Parcel;

import java.lang.reflect.Method;

public class ParcelUtils {
    public static boolean safeEnforceInterface(Parcel parcel, String descriptor) {
        try {
            parcel.enforceInterface(descriptor);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private static Method obtainMethod;

    @SuppressLint("SoonBlockedPrivateApi")
    public static Parcel fromNativePointer(long ptr) {
        if (ptr == 0) return null;

        if (obtainMethod == null) {
            try {
                //noinspection JavaReflectionMemberAccess
                obtainMethod = Parcel.class.getDeclaredMethod("obtain", long.class);
                obtainMethod.setAccessible(true);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return (Parcel) obtainMethod.invoke(null, ptr);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
