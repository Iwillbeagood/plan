import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    setNamespace("navigation")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.material.icon)
    implementation(projects.core.res)
    implementation(projects.core.designsystem)
}