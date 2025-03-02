import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.hilt)
    alias(libs.plugins.jun.android.room)
    id("kotlinx-serialization")
}

android {
    setNamespace("database")
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.utils)
    implementation(libs.junit4)
    implementation(libs.androidx.test.ext)
    implementation(libs.hilt.android.testing)
    implementation(libs.coroutines.test)
    implementation(libs.kotlinx.serialization.json)
    implementation(kotlin("reflect"))
}