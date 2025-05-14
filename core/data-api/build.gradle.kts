import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
}

android {
    setNamespace("dataApi")
}

dependencies {
    implementation(projects.core.model)
}
