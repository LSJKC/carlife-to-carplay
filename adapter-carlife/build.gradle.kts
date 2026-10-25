plugins {
    id("com.android.library")
}

android {
    namespace = "com.carplay.adapter.carlife"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

dependencies {
    implementation(project(":core-iap2"))
    implementation(project(":core-airplay"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20240303")
}
