import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
}

android {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    namespace = "com.team.ui_xml"
    compileSdk = 35

    defaultConfig {
        minSdk = 30

        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            "\"${properties.getProperty("google_client_id")}\"",
        )
        buildConfigField("String", "FEEDBACK_URL", "\"${properties.getProperty("feedback_url")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.glide)
    implementation(libs.bundles.google)

    ksp(libs.hilt.android.compiler)

    lintChecks(project(":lint"))

    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.bundles.android.test)
    androidTestRuntimeOnly(libs.mannodermaus.junit5.runner)
}
