#include <sys/socket.h>
#include <fcntl.h>
#include <dlfcn.h>
#include <sys/mman.h>

#include "zygisk.h"
#include "logging.h"
#include "loader.h"
#include "config_impl.h"
#include "magisk_loader.h"
#include "symbol_cache.h"

namespace lspd {
    int allow_unload = 0;
    int *allowUnload = &allow_unload;

    class ZygiskModule : public zygisk::ModuleBase {
        JNIEnv *env_;
        zygisk::Api *api_;

        void onLoad(zygisk::Api *api, JNIEnv *env) override {
            env_ = env;
            api_ = api;
            MagiskLoader::Init();
            ConfigImpl::Init();
        }

        void preAppSpecialize(zygisk::AppSpecializeArgs *args) override {
            MagiskLoader::GetInstance()->OnNativeForkAndSpecializePre(
                    env_, args->uid, args->gids, args->nice_name,
                    args->is_child_zygote ? *args->is_child_zygote : false, args->app_data_dir);
        }

        void postAppSpecialize(const zygisk::AppSpecializeArgs *args) override {
            MagiskLoader::GetInstance()->OnNativeForkAndSpecializePost(env_, args->nice_name, args->app_data_dir);
            if (*allowUnload) api_->setOption(zygisk::DLCLOSE_MODULE_LIBRARY);
        }

        void preServerSpecialize([[maybe_unused]] zygisk::ServerSpecializeArgs *args) override {
            MagiskLoader::GetInstance()->OnNativeForkSystemServerPre(env_);
        }

        void postServerSpecialize([[maybe_unused]] const zygisk::ServerSpecializeArgs *args) override {
            if (__system_property_find("ro.vendor.product.ztename")) {
                auto *process = env_->FindClass("android/os/Process");
                auto *set_argv0 = env_->GetStaticMethodID(process, "setArgV0",
                                                          "(Ljava/lang/String;)V");
                auto *name = env_->NewStringUTF("system_server");
                env_->CallStaticVoidMethod(process, set_argv0, name);
                env_->DeleteLocalRef(name);
                env_->DeleteLocalRef(process);
            }
            MagiskLoader::GetInstance()->OnNativeForkSystemServerPost(env_);
            if (*allowUnload) api_->setOption(zygisk::DLCLOSE_MODULE_LIBRARY);
        }
    };
} //namespace lspd

REGISTER_ZYGISK_MODULE(lspd::ZygiskModule);
