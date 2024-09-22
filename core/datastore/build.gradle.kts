import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
    id("kotlinx-serialization")
}

android {
   setNamespace("datastore")
}

dependencies {
    implementation(projects.core.utils)
    implementation(projects.core.model)

    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.immutable)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.test)
}
