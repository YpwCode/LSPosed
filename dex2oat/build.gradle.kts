plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "org.mliboot.dex2oat"

    buildFeatures {
        androidResources = false
        buildConfig = false
        prefab = true
        prefabPublishing = true
    }

    defaultConfig {
        minSdk = 29
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }

    prefab {
        register("dex2oat")
    }
}
