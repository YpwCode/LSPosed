#pragma once

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wgnu-string-literal-operator-template"

#include <string>
#include <filesystem>
#include <sys/system_properties.h>
#include <unistd.h>
#include <sys/stat.h>
#include "logging.h"

namespace lspd {
    using namespace std::literals::string_literals;

    inline int32_t GetAndroidApiLevel() {
        static int32_t api_level = []() {
            char prop_value[PROP_VALUE_MAX];
            __system_property_get("ro.build.version.sdk", prop_value);
            int base = atoi(prop_value);
            __system_property_get("ro.build.version.preview_sdk", prop_value);
            return base + atoi(prop_value);
        }();
        return api_level;
    }

    inline std::string JavaNameToSignature(std::string s) {
        std::replace(s.begin(), s.end(), '.', '/');
        return "L" + s;
    }
}

#pragma clang diagnostic pop
