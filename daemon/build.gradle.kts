import com.android.build.api.dsl.ApplicationExtension
import com.android.ide.common.signing.KeystoreHelper
import java.io.PrintStream

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.lsplugin.resopt)
}

val daemonName = "Mlpp"

val injectedPackageName: String by rootProject.extra
val injectedPackageUid: Int by rootProject.extra

val agpVersion: String by project

val defaultManagerPackageName: String by rootProject.extra

android {
    buildFeatures {
        prefab = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "org.mliboot.daemon"

        buildConfigField(
            "String",
            "DEFAULT_MANAGER_PACKAGE_NAME",
            """"$defaultManagerPackageName""""
        )
        buildConfigField("String", "MANAGER_INJECTED_PKG_NAME", """"$injectedPackageName"""")
        buildConfigField("int", "MANAGER_INJECTED_UID", """$injectedPackageUid""")
    }

    buildTypes {
        all {
            externalNativeBuild {
                cmake {
                    arguments += "-DANDROID_ALLOW_UNDEFINED_SYMBOLS=true"
                }
            }
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
        }
    }

    externalNativeBuild {
        cmake {
            path("src/main/jni/CMakeLists.txt")
        }
    }

    namespace = "org.mliboot.daemon"
}

android.applicationVariants.all {
    val variantCapped = name.replaceFirstChar { it.uppercase() }
    val variantLowered = name.lowercase()

    val outSrcDir =
        layout.buildDirectory.dir("generated/source/signInfo/${variantLowered}").get()
    val signInfoTask = tasks.register("generate${variantCapped}SignInfo") {
        dependsOn(":app:validateSigning${variantCapped}")
        val sign = rootProject.project(":app").extensions
            .getByType(ApplicationExtension::class.java)
            .buildTypes.named(variantLowered).get().signingConfig
        val outSrc = file("$outSrcDir/org/mliboot/mlspd/util/SignInfo.java")
        outputs.file(outSrc)
        doLast {
            outSrc.parentFile.mkdirs()
            val certificateInfo = KeystoreHelper.getCertificateInfo(
                sign?.storeType,
                sign?.storeFile,
                sign?.storePassword,
                sign?.keyPassword,
                sign?.keyAlias
            )
            PrintStream(outSrc).print(
                """
                |package org.mliboot.mlspd.util;
                |public final class SignInfo {
                |    public static final byte[] CERTIFICATE = {${
                    certificateInfo.certificate.encoded.joinToString(",")
                }};
                |}""".trimMargin()
            )
        }
    }
    registerJavaGeneratingTask(signInfoTask, outSrcDir.asFile)
}

dependencies {
    implementation(libs.libxposed.inter)
    implementation(libs.agp.apksig)
    implementation(libs.commons.lang3)
    implementation(projects.hiddenapi.bridge)
    implementation(projects.services.daemonService)
    implementation(projects.services.managerService)
    compileOnly(libs.androidx.annotation)
    compileOnly(projects.hiddenapi.stubs)
}
