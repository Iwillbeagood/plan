import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
}

android {
    setNamespace("data_api")
}

dependencies {
    implementation(projects.core.model)
}