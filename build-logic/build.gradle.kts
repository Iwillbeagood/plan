plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "jun.android.hilt"
            implementationClass = "jun.money.mate.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "jun.kotlin.hilt"
            implementationClass = "jun.money.mate.HiltKotlinPlugin"
        }
        register("androidRoom") {
            id = "jun.android.room"
            implementationClass = "jun.money.mate.AndroidRoomPlugin"
        }
    }
}