import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.compose)
}

android {
    setNamespace("designsystem")

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.landscapist.coil)
    implementation(libs.landscapist.animation)
    implementation(libs.androidx.compose.material.icon)

    implementation(projects.core.res)
    implementation(libs.androidx.activity.compose)
}