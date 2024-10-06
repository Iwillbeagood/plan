import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("home")
}

dependencies {
    implementation(projects.core.utils)
    implementation(projects.core.dataApi)
}