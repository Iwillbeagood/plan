import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.compose)
}

android {
    setNamespace("designsystem_date")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.landscapist.coil)
    implementation(libs.landscapist.animation)
    implementation(libs.snapper)
    implementation(libs.kotlinx.immutable)

    implementation(projects.core.res)
    implementation(projects.core.designsystem)
}