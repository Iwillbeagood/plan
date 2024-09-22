import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("main")
}

dependencies {
    implementation(projects.feature.home)

    implementation(projects.core.network)
    implementation(projects.core.stringRes)
    implementation(projects.core.utils)
}