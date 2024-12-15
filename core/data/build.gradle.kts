import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    alias(libs.plugins.jun.android.hilt)
    id("kotlinx-serialization")
}

android {
    setNamespace("data")

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.dataApi)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.network)
    implementation(projects.core.utils)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.immutable)
    implementation(libs.kotlinx.datetime)

    testImplementation(projects.core.testing)
}