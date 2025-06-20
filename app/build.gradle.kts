plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.personalizedlearningexperienceapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.personalizedlearningexperienceapp"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
//    implementation(libs.firebase.inappmessaging)
//    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.volley)
//    implementation(libs.firebase.appdistribution.gradle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.okhttp)
    implementation (libs.gson)
    implementation ("com.google.android.gms:play-services-wallet:19.1.0")
    implementation ("androidx.cardview:cardview:1.0.0")
//    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.squareup.okhttp3:okhttp:3.14.+")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}