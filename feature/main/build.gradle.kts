import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("main")
}

dependencies {
    implementation(projects.feature.splash)
    implementation(projects.feature.home)
    implementation(projects.feature.income)

    implementation(projects.core.network)
    implementation(projects.core.utils)
}