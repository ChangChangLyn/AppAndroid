

plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.baitap.viettel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.baitap.viettel"
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
    buildFeatures{
        viewBinding=true
    }
    packagingOptions {
        exclude("META-INF/NOTICE.md")

        exclude("META-INF/LICENSE.md")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //crop image
    api ("com.theartofdev.edmodo:android-image-cropper:2.8.0")
    implementation ("com.theartofdev.edmodo:android-image-cropper:2.8.0")
    //loading image in imageView
    implementation ("com.squareup.picasso:picasso:2.8")

    // send otp email
    implementation (libs.sms.confirmation.view)
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
}