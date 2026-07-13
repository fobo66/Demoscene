import com.android.sdklib.AndroidVersion
import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composePlugin)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

android {
    namespace = "dev.fobo66.andgopher"
    compileSdk {
        version =
            release(AndroidVersion.VersionCodes.CINNAMON_BUN) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        buildToolsVersion = "37.0.0"
        applicationId = "dev.fobo66.demoscene"
        minSdk {
            version = release(AndroidVersion.VersionCodes.BAKLAVA)
        }
        targetSdk {
            version = release(AndroidVersion.VersionCodes.CINNAMON_BUN)
        }
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType<Detekt> {
    jvmTarget = "17"
}

detekt {
    autoCorrect = true
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.core.splashscreen)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.navigation3)
    implementation(libs.coroutines.android)
    implementation(libs.collections)
    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material3.adaptive)
    implementation(libs.material3.adaptive.layout)
    implementation(libs.material3.adaptive.navigation)
    implementation(libs.kermit)
    testImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
