import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("splash")
}

dependencies {
    implementation(projects.core.dataApi)
}