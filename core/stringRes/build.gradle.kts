import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
}

android {
    setNamespace("stringres")

    buildFeatures {
        buildConfig = true
    }
}