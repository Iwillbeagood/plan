pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "moneymate"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

// core
include(
    ":core:data",
    ":core:data-api",
    ":core:database",
    ":core:datastore",
    ":core:designsystem",
    ":core:designsystem-date",
    ":core:domain",
    ":core:model",
    ":core:navigation",
    ":core:network",
    ":core:res",
    ":core:testing",
    ":core:ui",
    ":core:utils"
)

// feature
include(
    ":feature:main",
    ":feature:splash",

    ":feature:home",
    ":feature:income",
    ":feature:save",
    ":feature:spending-plan",
    ":feature:comsumption-spend",
)
