

//
// Created by kotori on 2/4/21.
//

#ifndef LSPOSED_NATIVE_API_H
#define LSPOSED_NATIVE_API_H

#include <cstdint>
#include <string>

#include "utils/hook_helper.hpp"

typedef int (*HookFunType)(void *func, void *replace, void **backup);

typedef int (*UnhookFunType)(void *func);

typedef void (*NativeOnModuleLoaded)(const char *name, void *handle);

typedef struct {
    uint32_t version;
    HookFunType hookFunc;
    UnhookFunType unhookFunc;
} NativeAPIEntries;

typedef NativeOnModuleLoaded (*NativeInit)(const NativeAPIEntries *entries);

namespace lspd {
    bool InstallNativeAPI(const lsplant::HookHandler& handler);

    void RegisterNativeLib(const std::string &library_name);
}

#endif //LSPOSED_NATIVE_API_H
