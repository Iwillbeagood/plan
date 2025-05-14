import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.compose)
}

android {
    setNamespace("ui")
}

dependencies {
    api(projects.core.model)
    api(projects.core.res)
    api(projects.core.designsystem)
    api(projects.core.utils)
    api(projects.core.navigation)
    api(libs.androidx.compose.material.icon)

    implementation(projects.core.designsystemDate)
}
