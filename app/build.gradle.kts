plugins {
    id ("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.taskmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.taskmanager"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.0")
    implementation("com.google.firebase:firebase-appcheck:18.0.0")
    implementation("com.google.firebase:firebase-appcheck-playintegrity:18.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.firebase:firebase-firestore:25.0.0")


    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.fragment:fragment:1.7.1")
    implementation("androidx.viewpager:viewpager:1.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
}
