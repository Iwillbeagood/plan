plugins {
    alias(libs.plugins.jun.kotlin.library)
    id("kotlinx-serialization")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}