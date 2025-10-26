import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
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

    namespace = "com.team.todoktodok"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.team.todoktodok"
        minSdk = 30
        targetSdk = 35
        versionCode = 10
        versionName = "1.0.7"

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
    lint {
        disable += "NullSafeMutableLiveData"
        disable += "ComposeViewModelForwarding"
    }

    signingConfigs {
        create("release") {
            storeFile = file(properties.getProperty("release_store_file"))
            storePassword = properties.getProperty("release_store_password")
            keyAlias = properties.getProperty("release_key_alias")
            keyPassword = properties.getProperty("release_key_password")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false

            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${properties.getProperty("debug_base_url")}\"",
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${properties.getProperty("release_base_url")}\"",
            )
            signingConfig = signingConfigs.getByName("release")
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
        viewBinding = true
        buildConfig = true
        compose = true
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) {
        it.packaging.resources.excludes
            .add("META-INF/**")
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.network)
    implementation(libs.bundles.google)
    implementation(libs.bundles.glide)
    implementation(libs.bundles.logging)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.bundles.android.test)
    androidTestRuntimeOnly(libs.mannodermaus.junit5.runner)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt.android)

    androidTestImplementation(libs.hilt.android.testing)
    testImplementation(libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    lintChecks(libs.compose.lint)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.compose.test)
    debugImplementation(libs.bundles.compose.debug)
}
