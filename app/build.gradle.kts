plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "io.aethibo.combatcoach"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "io.aethibo.combatcoach"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // ── Compose BOM ──────────────────────────────────────────────────────────
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // ── Compose ──────────────────────────────────────────────────────────────
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // ── Android ───────────────────────────────────────────────────────────
    implementation(libs.androidx.core.splashscreen)

    // ── Navigation ───────────────────────────────────────────────────────────
    implementation(libs.navigation.compose)

    // ── Lifecycle ────────────────────────────────────────────────────────────
    // collectAsStateWithLifecycle — no ViewModel dependency needed
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)

    // ── Room ─────────────────────────────────────────────────────────────────
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // ── Koin ─────────────────────────────────────────────────────────────────
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    // ── Arrow ─────────────────────────────────────────────────────────────────
    implementation(libs.bundles.arrow)

    // ── Kotlinx ──────────────────────────────────────────────────────────────
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    // ── Logging ──────────────────────────────────────────────────────────────
    implementation(libs.timber)

    // ── Unit tests ───────────────────────────────────────────────────────────
    testImplementation(libs.bundles.testing.unit)

    // ── Android / UI tests ───────────────────────────────────────────────────
    androidTestImplementation(libs.bundles.testing.android)
}