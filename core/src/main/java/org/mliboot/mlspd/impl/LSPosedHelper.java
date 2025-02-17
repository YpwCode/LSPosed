package org.mliboot.mlspd.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.mliboot.libxp.api.IXpInter;
import org.mliboot.libxp.api.errors.HandlerFailedError;

public class LSPosedHelper {

    @SuppressWarnings("UnusedReturnValue")
    public static <T> IXpInter.MethodUnhooker<Method>
    hookMethod(Class<? extends IXpInter.Hooker> hooker, Class<T> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            var method = clazz.getDeclaredMethod(methodName, parameterTypes);
            return LSPosedBridge.doHook(method, IXpInter.PRIORITY_DEFAULT, hooker);
        } catch (NoSuchMethodException e) {
            throw new HandlerFailedError(e);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <T> Set<IXpInter.MethodUnhooker<Method>>
    hookAllMethods(Class<? extends IXpInter.Hooker> hooker, Class<T> clazz, String methodName) {
        var unhooks = new HashSet<IXpInter.MethodUnhooker<Method>>();
        for (var method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                unhooks.add(LSPosedBridge.doHook(method, IXpInter.PRIORITY_DEFAULT, hooker));
            }
        }
        return unhooks;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <T> IXpInter.MethodUnhooker<Constructor<T>>
    hookConstructor(Class<? extends IXpInter.Hooker> hooker, Class<T> clazz, Class<?>... parameterTypes) {
        try {
            var constructor = clazz.getDeclaredConstructor(parameterTypes);
            return LSPosedBridge.doHook(constructor, IXpInter.PRIORITY_DEFAULT, hooker);
        } catch (NoSuchMethodException e) {
            throw new HandlerFailedError(e);
        }
    }
}
