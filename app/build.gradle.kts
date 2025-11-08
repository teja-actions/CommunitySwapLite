plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android") // keep Kotlin plugin if needed
}

android {
    namespace = "com.example.communityhub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.communityhub"
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

    // ❌ Disable Compose
    buildFeatures {
        compose = false
        viewBinding = true
    }
}

dependencies {
    // --- Core Android Libraries ---

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // --- RecyclerView + CardView ---
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    // --- Optional Preferences ---
    implementation("androidx.preference:preference:1.2.1")

    // --- Testing (optional) ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
