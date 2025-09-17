import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    tasks.named("build") {
        dependsOn("ktlintCheck")
    }

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    defaultConfig {
        buildConfigField(
            "String",
            "BASE_URL",
            "\"${properties.getProperty("base_url")}\"",
        )

        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            "\"${properties.getProperty("google_client_id")}\"",
        )

        buildConfigField(
            "String",
            "FEEDBACK_URL",
            "\"${properties.getProperty("feedback_url")}\"",
        )
    }

    namespace = "com.team.todoktodok"
    compileSdk = 35

    lint {
        disable += "NullSafeMutableLiveData"
    }

    defaultConfig {
        applicationId = "com.team.todoktodok"
        minSdk = 30
        targetSdk = 35
        versionCode = 5
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("debug")
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

    packaging {
        resources {
            excludes += "META-INF/**"
            excludes += "win32-x86*/**"
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.network)
    implementation(libs.bundles.google)
    implementation(libs.bundles.glide)
    implementation(libs.bundles.logging)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.core.testing)
    ksp(libs.androidx.room.compiler)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.android.test)
    androidTestRuntimeOnly(libs.mannodermaus.junit5.runner)
}
