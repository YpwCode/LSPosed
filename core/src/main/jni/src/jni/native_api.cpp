

//
// Created by 双草酸酯 on 2/7/21.
//
#include "native_api.h"
#include "native_util.h"
#include "utils/jni_helper.hpp"
#include "../native_api.h"

using namespace lsplant;

namespace lspd {
    LSP_DEF_NATIVE_METHOD(void, NativeAPI, recordNativeEntrypoint, jstring jstr) {
        lsplant::JUTFString str(env, jstr);
        RegisterNativeLib(str);
    }

    static JNINativeMethod gMethods[] = {
            LSP_NATIVE_METHOD(NativeAPI, recordNativeEntrypoint, "(Ljava/lang/String;)V")
    };

    void RegisterNativeAPI(JNIEnv *env) {
        REGISTER_LSP_NATIVE_METHODS(NativeAPI);
    }
}
