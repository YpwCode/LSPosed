#include <jni.h>
#include <cstring>
#include <cstdlib>
#include <array>
#include "logging.h"
#include "loader.h"
#include "config_impl.h"
#include "magisk_loader.h"
#include "symbol_cache.h"

#define RIRU_MODULE
#include "riru.h"

namespace lspd {
    int *allowUnload = nullptr;
    namespace {
        std::string magiskPath;

        jstring nice_name = nullptr;
        jstring app_dir = nullptr;

        void onModuleLoaded() {
            LOGI("onModuleLoaded: welcome to LSPosed!");
            LOGI("onModuleLoaded: version v{} ({})", versionName, versionCode);
            MagiskLoader::Init();
            ConfigImpl::Init();
        }

        void nativeForkAndSpecializePre(JNIEnv *env, jclass, jint *_uid, jint *,
                                        jintArray *gids, jint *,
                                        jobjectArray *, jint *,
                                        jstring *, jstring *_nice_name,
                                        jintArray *, jintArray *,
                                        jboolean *start_child_zygote, jstring *,
                                        jstring *_app_data_dir, jboolean *,
                                        jobjectArray *,
                                        jobjectArray *,
                                        jboolean *,
                                        jboolean *) {
            nice_name = *_nice_name;
            app_dir = *_app_data_dir;
            MagiskLoader::GetInstance()->OnNativeForkAndSpecializePre(env, *_uid, *gids,
                                                                 nice_name,
                                                                 *start_child_zygote,
                                                                 *_app_data_dir);
        }

        void nativeForkAndSpecializePost(JNIEnv *env, jclass, jint res) {
            if (res == 0)
                MagiskLoader::GetInstance()->OnNativeForkAndSpecializePost(env, nice_name, app_dir);
        }

        void nativeForkSystemServerPre(JNIEnv *env, jclass, uid_t *, gid_t *,
                                       jintArray *, jint *,
                                       jobjectArray *, jlong *,
                                       jlong *) {
            MagiskLoader::GetInstance()->OnNativeForkSystemServerPre(env);
        }

        void nativeForkSystemServerPost(JNIEnv *env, jclass, jint res) {
            if (res == 0)
                MagiskLoader::GetInstance()->OnNativeForkSystemServerPost(env);
        }

        /* method added in Android Q */
        void specializeAppProcessPre(JNIEnv *env, jclass, jint *_uid, jint *,
                                     jintArray *gids, jint *,
                                     jobjectArray *, jint *,
                                     jstring *, jstring *_nice_name,
                                     jboolean *start_child_zygote, jstring *,
                                     jstring *_app_data_dir, jboolean *,
                                     jobjectArray *,
                                     jobjectArray *,
                                     jboolean *,
                                     jboolean *) {
            nice_name = *_nice_name;
            app_dir = *_app_data_dir;
            MagiskLoader::GetInstance()->OnNativeForkAndSpecializePre(env, *_uid, *gids,
                                                                 nice_name,
                                                                 *start_child_zygote,
                                                                 *_app_data_dir);
        }

        void specializeAppProcessPost(JNIEnv *env, jclass) {
            MagiskLoader::GetInstance()->OnNativeForkAndSpecializePost(env, nice_name, app_dir);
        }
    }

    RiruVersionedModuleInfo module{
            .moduleApiVersion = apiVersion,
            .moduleInfo = RiruModuleInfo{
                    .supportHide = !isDebug,
                    .version = versionCode,
                    .versionName = versionName,
                    .onModuleLoaded = lspd::onModuleLoaded,
                    .forkAndSpecializePre = lspd::nativeForkAndSpecializePre,
                    .forkAndSpecializePost = lspd::nativeForkAndSpecializePost,
                    .forkSystemServerPre = lspd::nativeForkSystemServerPre,
                    .forkSystemServerPost = lspd::nativeForkSystemServerPost,
                    .specializeAppProcessPre = lspd::specializeAppProcessPre,
                    .specializeAppProcessPost = lspd::specializeAppProcessPost,
            }
    };
}

RIRU_EXPORT RiruVersionedModuleInfo *init(Riru *riru) {
    LOGD("using riru {}", riru->riruApiVersion);
    LOGD("module path: {}", riru->magiskModulePath);
    lspd::magiskPath = riru->magiskModulePath;
    if (!lspd::isDebug && lspd::magiskPath.find(lspd::moduleName) == std::string::npos) {
        LOGE("who am i");
        return nullptr;
    }
    lspd::allowUnload = riru->allowUnload;
    return &lspd::module;
}
