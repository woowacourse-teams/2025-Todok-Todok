import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    namespace = "com.team.data"
    compileSdk {
        version = release(35)
    }

    defaultConfig {
        minSdk = 30

        buildConfigField(
            "String",
            "BASE_URL",
            "\"${properties.getProperty("debug_base_url")}\"",
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }

    buildTypes {
        release {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${properties.getProperty("release_base_url")}\"",
            )
        }
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.network)
    implementation(libs.bundles.hilt)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)

    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.bundles.android.test)
    androidTestRuntimeOnly(libs.mannodermaus.junit5.runner)
}
