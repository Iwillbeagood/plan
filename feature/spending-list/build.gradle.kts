import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("spending_list")
}

dependencies {
    implementation(projects.core.dataApi)
}