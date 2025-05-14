import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.compose)
}

android {
    setNamespace("network")
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
}
