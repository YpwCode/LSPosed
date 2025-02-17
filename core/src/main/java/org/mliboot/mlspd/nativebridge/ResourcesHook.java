

package org.mliboot.mlspd.nativebridge;

import android.content.res.Resources;
import android.content.res.XResources;

import dalvik.annotation.optimization.FastNative;

public class ResourcesHook {

    public static native boolean initXResourcesNative();

    public static native boolean makeInheritable(Class<?> clazz);

    public static native ClassLoader buildDummyClassLoader(ClassLoader parent, String resourceSuperClass, String typedArraySuperClass);

    @FastNative
    public static native void rewriteXmlReferencesNative(long parserPtr, XResources origRes, Resources repRes);
}
