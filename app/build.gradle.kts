plugins {
    id("com.android.application")
}

android {
    namespace = "com.carlink.cast"
    compileSdk = 36

    /**
     * One fixed signing key for every build.
     *
     * Debug builds used to carry the throw-away `CN=Android Debug` key that the
     * build tooling regenerates per machine. Android refuses to install an APK
     * whose signer differs from the installed copy, so an update over a build
     * made elsewhere — or after the debug key was recreated — failed with a bare
     * "安装失败". This key never changes, so updates apply cleanly.
     */
    signingConfigs {
        create("stable") {
            storeFile = rootProject.file("carlink-release.jks")
            storePassword = "carlink2026"
            keyAlias = "carlink"
            keyPassword = "carlink2026"
        }
    }

    buildTypes {
        getByName("debug") { signingConfig = signingConfigs.getByName("stable") }
        getByName("release") {
            signingConfig = signingConfigs.getByName("stable")
            isMinifyEnabled = false
        }
    }

    defaultConfig {
        applicationId = "com.carlink.cast"
        // WifiNetworkSpecifier and AudioPlaybackCapture both need Android 10,
        // and the whole session is built on them.
        minSdk = 29
        targetSdk = 34
        // Single source of truth for the version the phone displays. These used
        // to stay at 100/"1.0.0" while the APK file was renamed by hand, so
        // every build installed as 1.0.0 and an update over an existing install
        // looked like the same version to the package manager — which is what
        // made installing over an older build fail.
        versionCode = 307
        versionName = "1.3.7"
        buildConfigField("String", "BUILD_ID", "\"carlink-cast-1.3.7-307-builtin-wallpapers-only\"")
    }

    buildFeatures { buildConfig = true }
    testOptions { unitTests.isReturnDefaultValues = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            output.outputFileName.set("carlink-cast-${android.defaultConfig.versionName}.apk")
        }
    }
}

dependencies {
    implementation(project(":core-iap2"))
    implementation(project(":core-airplay"))
    implementation(project(":adapter-carlife"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    // mDNS on Android 10–12, where NsdManager cannot be bound to the vehicle
    // network and the default network would discover the wrong interface.
    implementation("org.jmdns:jmdns:3.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20240303")
}
