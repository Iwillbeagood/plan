import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val currentTime = SimpleDateFormat("yyyy.MM.dd")
                currentTime.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                output.outputFileName = "[${variant.versionName}] 뺙통_${currentTime.format(Date())}.apk"
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
    implementation(projects.core.stringRes)

    implementation(projects.feature.main)

    implementation(libs.firebase.core)
    implementation(libs.firebase.messaging)
    implementation(libs.gson)
}