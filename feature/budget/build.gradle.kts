import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("budget")
}

dependencies {
    implementation(projects.core.designsystemDate)
}