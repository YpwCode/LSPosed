

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
    namespace = "org.mliboot.mlspd.managerservice"
}

dependencies {
    api(libs.rikkax.parcelablelist)
}
