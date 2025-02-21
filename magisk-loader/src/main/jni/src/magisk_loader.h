//
// Created by Nullptr on 2022/3/16.
//

#pragma once

#include "context.h"

namespace lspd {
    class MagiskLoader : public Context {
    public:
        inline static void Init() {
            instance_ = std::make_unique<MagiskLoader>();
        }

        inline static MagiskLoader *GetInstance() {
            return static_cast<MagiskLoader*>(instance_.get());
        }

        void OnNativeForkAndSpecializePre(JNIEnv *env, jint uid, jintArray &gids, jstring nice_name,
                                          jboolean is_child_zygote, jstring app_data_dir);

        void OnNativeForkAndSpecializePost(JNIEnv *env, jstring nice_name, jstring app_dir);

        void OnNativeForkSystemServerPost(JNIEnv *env);

        void OnNativeForkSystemServerPre(JNIEnv *env);

    protected:
        void LoadDex(JNIEnv *env, PreloadedDex &&dex) override;

        void SetupEntryClass(JNIEnv *env) override;

    private:
        bool skip_ = false;

        static void setAllowUnload(bool unload);
    };
} // namespace lspd
