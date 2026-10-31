import java.util.Properties

plugins {
    id("com.android.application")
}

android {
    namespace = "com.carlink.cast"
    compileSdk = 36

    /**
     * Signing key, read from a local file that is never committed.
     *
     * `keystore.properties` (already in .gitignore) supplies:
     *
     *     storeFile=carlink-release.jks
     *     storePassword=…
     *     keyAlias=carlink
     *     keyPassword=…
     *
     * When it is absent — which is the case for anyone who clones this repo —
     * the build falls back to the standard debug key. That keeps a fresh
     * checkout building without shipping a private key or its password in the
     * source tree.
     */
    val keystorePropsFile = rootProject.file("keystore.properties")
    val keystoreProps = Properties().apply {
        if (keystorePropsFile.exists()) keystorePropsFile.inputStream().use { load(it) }
    }
    val hasReleaseKeystore = keystoreProps.getProperty("storeFile") != null

    signingConfigs {
        if (hasReleaseKeystore) {
            create("stable") {
                storeFile = rootProject.file(keystoreProps.getProperty("storeFile"))
                storePassword = keystoreProps.getProperty("storePassword")
                keyAlias = keystoreProps.getProperty("keyAlias")
                keyPassword = keystoreProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        getByName("debug") { signingConfig = signingConfigs.getByName(if (hasReleaseKeystore) "stable" else "debug") }
        getByName("release") {
            signingConfig = signingConfigs.getByName(if (hasReleaseKeystore) "stable" else "debug")
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
        versionCode = 330
        versionName = "1.3.30"
        buildConfigField("String", "BUILD_ID", "\"carlink-cast-1.3.30-330-card-and-snap\"")
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
