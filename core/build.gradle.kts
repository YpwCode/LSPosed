val verName: String by rootProject.extra
val verCode: Int by rootProject.extra

plugins {
    alias(libs.plugins.agp.lib)
}

android {

    namespace = "org.mliboot.mlspd.core"

    buildFeatures {
        androidResources = false
        buildConfig = true
    }

    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")

        buildConfigField("String", "FRAMEWORK_NAME", """"${rootProject.name}"""")
        buildConfigField("String", "VERSION_NAME", """"$verName"""")
        buildConfigField("long", "VERSION_CODE", """$verCode""")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }
}

copy {
    from("src/main/jni/template/") {
        expand("VERSION_CODE" to "$verCode", "VERSION_NAME" to verName)
    }
    into("src/main/jni/src/")
}

dependencies {
    api(libs.libxposed.api)
    implementation(libs.commons.lang3)
    implementation(libs.axml)
    implementation(projects.hiddenapi.bridge)
    implementation(projects.services.daemonService)
    implementation(projects.services.managerService)
    compileOnly(libs.androidx.annotation)
    compileOnly(projects.hiddenapi.stubs)
}
