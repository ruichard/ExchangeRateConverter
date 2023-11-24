plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.allopen")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.exchangerate.converter"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.exchangerate.converter"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }
    secrets {
        defaultPropertiesFileName = "secrets.properties"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    allOpen {
        annotation("androidx.annotation.OpenForTesting")
    }
}
dependencies {
    implementation(libs.coreKtx)
    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.annotation)
    implementation(libs.activityKtx)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converterGson)
    implementation(libs.lifecycleViewModelKtx)
    implementation(libs.lifecycleLiveDataKtx)
    implementation(libs.lifecycleRuntimeKtx)
    implementation(libs.lifecycleViewModelSavedState)
    implementation(libs.constraintlayout)
    implementation(platform(libs.composeBom))
    androidTestImplementation(platform(libs.composeBom))
    debugImplementation(libs.ui.test.manifest)
    kapt(libs.lifecycleCompiler)
    implementation(libs.lifecycleCommonJava8)
    implementation(libs.hiltAndroid)
    kapt(libs.hiltAndroidCompiler)
    kapt(libs.hiltCompiler)
    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)
    implementation(libs.mmkv)
    testImplementation(libs.junit)
    testImplementation(libs.coroutinesTest)
    testImplementation(libs.mockWebServer)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.coreTesting)
    androidTestImplementation(libs.espressoCore)
    androidTestImplementation(libs.espressoContrib)
    androidTestImplementation(libs.testCore)

    implementation(libs.activityCompose)
    implementation(platform(libs.composeBom))
    implementation(libs.composeUi)
    implementation(libs.composeUiGraphics)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeMaterial3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.navigation.compose)

    androidTestImplementation(platform(libs.composeBom))
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTooling)
//    androidTestImplementation(libs.composeUiTestManifest)
}
