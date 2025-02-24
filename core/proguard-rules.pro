-keep class org.md.xlib.** {*;}
-keep class org.mliboot.libxp.** {*;}
-keepattributes RuntimeVisibleAnnotations
-keep class android.** { *; }
-keepclasseswithmembers,includedescriptorclasses class * {
    native <methods>;
}
-keepclassmembers class org.mliboot.mlspd.impl.LSPosedContext {
    public <methods>;
}
-keepclassmembers class org.mliboot.mlspd.impl.LSPosedHookCallback {
    public <methods>;
}
-keep,allowoptimization,allowobfuscation @org.mliboot.libxp.api.annotations.* class * {
    @org.mliboot.libxp.api.annotations.BeforeInvocation <methods>;
    @org.mliboot.libxp.api.annotations.AfterInvocation <methods>;
}
-keepclassmembers class org.mliboot.mlspd.impl.LSPosedBridge$NativeHooker {
    <init>(java.lang.reflect.Executable);
    callback(...);
}
-keepclassmembers class org.mliboot.mlspd.impl.LSPosedBridge$HookerCallback {
    final *** beforeInvocation;
    final *** afterInvocation;
    HookerCallback(...);
}
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
}
-repackageclasses
-allowaccessmodification
-dontwarn org.slf4j.impl.StaticLoggerBinder
