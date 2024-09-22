import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.compose)
}

android {
    setNamespace("ui")
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.stringRes)
    implementation(projects.core.designsystem)
    implementation(projects.core.stringRes)
    implementation(projects.core.utils)
}