import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.library)
}

android {
    setNamespace("testing")

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
            )
        }
    }
}

dependencies {
    api(libs.junit4)
    api(libs.junit.vintage.engine)
    api(libs.kotlin.test)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.coroutines.test)
    api(libs.androidx.compose.ui.test)
    api(libs.mockwebserver)
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.runner)
}
