import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("spending_plan")
}

dependencies {
    implementation(projects.core.dataApi)
    implementation(projects.core.designsystemDate)
}