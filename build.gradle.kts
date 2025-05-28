// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.spotless)
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint()
                .setEditorConfigPath("$rootDir/.editorconfig")
                .editorConfigOverride(
                    mapOf(
                        "ktlint_standard_enum-entry-name-case" to "disabled",
                        "ktlint_standard_class-naming" to "disabled",
                        "ktlint_standard_function-naming" to "disabled",
                    ),
                )
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
    }
}

apply {
    from("gradle/dependencyGraph.gradle")
}
