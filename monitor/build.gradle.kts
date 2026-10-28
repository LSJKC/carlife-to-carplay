plugins {
    id("com.android.application")
}

android {
    namespace = "com.carlink.monitor"
    compileSdk = 36

    // Same fixed key as the cast app, so a rebuild always installs over the
    // previous copy instead of failing with a bare "安装失败".
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
        applicationId = "com.carlink.monitor"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            output.outputFileName.set("carlife-monitor-${android.defaultConfig.versionName}.apk")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    testImplementation("junit:junit:4.13.2")
}
