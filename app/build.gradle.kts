plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.gms.google.services)
}

// load TMDB API key from gradle.properties
val tmdb_api_key: String? = project.findProperty("TMDB_API_KEY") as String?

android {
    namespace = "com.mbialowas.moviehub2025"
    compileSdk = 35

    defaultConfig {
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdb_api_key\"")
        applicationId = "com.mbialowas.moviehub2025"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    //google maps
    implementation(libs.play.services.maps.v1820)  // Google Maps
    implementation(libs.play.services.location) // Location Services
    implementation(libs.places.v330) // Places API

    // accompanist permissions
    implementation(libs.accompanist)

    // google maps composable
    implementation(libs.google.maps.sdk) // Google Maps SDK
    implementation(libs.maps.compose)    // Compose Maps Library
    implementation (libs.androidx.compose.material) // Had to add bc or Marker composable

    // google play services
    implementation(libs.play.services.maps)



    // moshi
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)

    //coil compose
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //room
    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.firebase.firestore)
    annotationProcessor(libs.androidx.room.room.compiler)
    ksp("androidx.room:room-compiler:2.6.1")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}