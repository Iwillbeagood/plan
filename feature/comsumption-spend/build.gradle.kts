import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("consumption_spend")
}

dependencies {
    implementation(projects.core.designsystemDate)
}