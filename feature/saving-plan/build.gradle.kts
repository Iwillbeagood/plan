import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("saving_plan")
}

dependencies {
    implementation(projects.core.dataApi)
}