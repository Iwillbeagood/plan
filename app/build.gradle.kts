plugins {
    alias(libs.plugins.jun.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "jun.money.mate"

    defaultConfig {
        applicationId = "jun.money.mate"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.utils)
    implementation(projects.core.model)
    implementation(projects.core.stringRes)

    implementation(projects.feature.main)

    implementation(libs.firebase.core)
    implementation(libs.firebase.messaging)
    implementation(libs.gson)
}