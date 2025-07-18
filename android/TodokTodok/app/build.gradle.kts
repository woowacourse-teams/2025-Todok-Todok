plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.todoktodok"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.todoktodok"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    packaging {
        resources {
            excludes += "META-INF/**"
            excludes += "win32-x86*/**"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.network)
    implementation(libs.bundles.google)
    implementation(libs.bundles.glide)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)
    androidTestRuntimeOnly(libs.mannodermaus.junit5.runner)
}
