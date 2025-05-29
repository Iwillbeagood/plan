plugins {
    alias(libs.plugins.jun.android.application)
}

android {
    namespace = "jun.money.mate"

    defaultConfig {
        applicationId = "jun.money.mate"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
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
    implementation(projects.core.dataApi)
    implementation(projects.core.utils)
    implementation(projects.core.model)
    implementation(projects.core.res)

    implementation(projects.feature.main)

    implementation(libs.firebase.core)
    implementation(libs.firebase.messaging)
    implementation(libs.gson)

    implementation(libs.work.runtime)
    implementation(libs.work.hilt)
    androidTestImplementation(libs.work.testing)
    androidTestImplementation(libs.work.runtime)
    androidTestImplementation(projects.core.testing)
}
