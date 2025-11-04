plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.team.core"
    compileSdk {
        version = release(35)
    }

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.bundles.androidx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.google.firebase.crashlytics.ktx)

    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.core.testing)
}
