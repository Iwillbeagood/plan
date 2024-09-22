import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
}

android {
    setNamespace("domain")
}

dependencies {
    implementation(projects.core.dataApi)
    implementation(projects.core.model)
    implementation(projects.core.utils)
    implementation(projects.core.datastore)
    testImplementation(projects.core.testing)

    implementation(libs.firebase.core)
    implementation(libs.firebase.messaging)
}