#pragma once

#include <jni.h>
#include <sys/types.h>
#include <string>
#include "macros.h"
#include "utils.h"
#include "utils/hook_helper.hpp"

namespace lspd {

//#define LOG_DISABLED
//#define DEBUG
    using lsplant::operator""_tstr;

    inline bool constexpr Is64() {
#if defined(__LP64__)
        return true;
#else
        return false;
#endif
    }

    inline constexpr bool is64 = Is64();

    inline bool constexpr IsDebug() {
#ifdef NDEBUG
        return false;
#else
        return true;
#endif
    }

    inline constexpr bool isDebug = IsDebug();

#if defined(__LP64__)
# define LP_SELECT(lp32, lp64) lp64
#else
# define LP_SELECT(lp32, lp64) lp32
#endif

    inline static constexpr auto kLibArtName = "libart.so"_tstr;
    inline static constexpr auto kLibFwName = "libandroidfw.so"_tstr;

    inline constexpr const char *BoolToString(bool b) {
        return b ? "true" : "false";
    }

    extern const int versionCode;
    extern const char* const versionName;
}
