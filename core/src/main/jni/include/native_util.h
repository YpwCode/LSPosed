#include <dlfcn.h>
#include "dobby.h"
#include <sys/mman.h>
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-value"
#pragma once

#include <context.h>
#include "macros.h"
#include "utils/jni_helper.hpp"
#include "logging.h"
#include "config.h"
#include <cassert>
#include "config_bridge.h"

namespace lspd {

[[gnu::always_inline]]
inline bool RegisterNativeMethodsInternal(JNIEnv *env,
                                          std::string_view class_name,
                                          const JNINativeMethod *methods,
                                          jint method_count) {

    auto clazz = Context::GetInstance()->FindClassFromCurrentLoader(env, class_name.data());
    if (clazz.get() == nullptr) {
        LOGF("Couldn't find class: {}", class_name.data());
        return false;
    }
    return JNI_RegisterNatives(env, clazz, methods, method_count);
}

#if defined(__cplusplus)
#define _NATIVEHELPER_JNI_MACRO_CAST(to) \
    reinterpret_cast<to>
#else
#define _NATIVEHELPER_JNI_MACRO_CAST(to) \
    (to)
#endif

#ifndef LSP_NATIVE_METHOD
#define LSP_NATIVE_METHOD(className, functionName, signature)                \
  { #functionName,                                                       \
    signature,                                                           \
    _NATIVEHELPER_JNI_MACRO_CAST(void*) (Java_org_mliboot_mlspd_nativebridge_## className ## _ ## functionName) \
  }
#endif

#define JNI_START [[maybe_unused]] JNIEnv* env, [[maybe_unused]] jclass clazz

#ifndef LSP_DEF_NATIVE_METHOD
#define LSP_DEF_NATIVE_METHOD(ret, className, functionName, ...)                \
  extern "C" ret Java_org_mliboot_mlspd_nativebridge_## className ## _ ## functionName (JNI_START, ##  __VA_ARGS__)
#endif

#define REGISTER_LSP_NATIVE_METHODS(class_name) \
  RegisterNativeMethodsInternal(env, GetNativeBridgeSignature() + #class_name, gMethods, arraysize(gMethods))

inline int HookFunction(void *original, void *replace, void **backup) {
    if constexpr (isDebug) {
        Dl_info info;
        if (dladdr(original, &info))
        LOGD("Hooking {} ({}) from {} ({})",
             info.dli_sname ? info.dli_sname : "(unknown symbol)", info.dli_saddr,
             info.dli_fname ? info.dli_fname : "(unknown file)", info.dli_fbase);
    }
    return DobbyHook(original, reinterpret_cast<dobby_dummy_func_t>(replace), reinterpret_cast<dobby_dummy_func_t *>(backup));
}

inline int UnhookFunction(void *original) {
    if constexpr (isDebug) {
        Dl_info info;
        if (dladdr(original, &info))
        LOGD("Unhooking {} ({}) from {} ({})",
             info.dli_sname ? info.dli_sname : "(unknown symbol)", info.dli_saddr,
             info.dli_fname ? info.dli_fname : "(unknown file)", info.dli_fbase);
    }
    return DobbyDestroy(original);
}

inline std::string GetNativeBridgeSignature() {
    const auto &obfs_map = ConfigBridge::GetInstance()->obfuscation_map();
    static auto signature = obfs_map.at("org.mliboot.mlspd.nativebridge.");
    return signature;
}

} // namespace lspd

#pragma clang diagnostic pop
