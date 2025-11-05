plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.team.ui_compose"
    compileSdk = 35

    defaultConfig {
        minSdk = 30

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
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.hilt)

    ksp(libs.hilt.android.compiler)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    lintChecks(libs.compose.lint)

    testImplementation(project(":data"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.core.testing)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.compose.test)
    androidTestRuntimeOnly(libs.mannodermaus.junit5.runner)
    debugImplementation(libs.bundles.compose.debug)
}
