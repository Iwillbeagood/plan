import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("spending")
}

dependencies {
    implementation(projects.core.designsystemDate)
}