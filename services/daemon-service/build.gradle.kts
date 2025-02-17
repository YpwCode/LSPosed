

plugins {
    alias(libs.plugins.agp.lib)
}

android {
    buildFeatures {
        aidl = true
    }

    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    aidlPackagedList += "org/mliboot/mlspd/models/Module.aidl"
    aidlPackagedList += "org/mliboot/mlspd/models/PreloadedApk.aidl"
    namespace = "org.mliboot.mlspd.daemonservice"
}

dependencies {
    compileOnly(projects.hiddenapi.stubs)
}
