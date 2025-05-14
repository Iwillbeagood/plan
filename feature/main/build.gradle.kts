import jun.money.mate.setNamespace

plugins {
    alias(libs.plugins.jun.android.feature)
}

android {
    setNamespace("main")
}

dependencies {
    implementation(projects.feature.splash)
    implementation(projects.feature.home)
    implementation(projects.feature.income)
    implementation(projects.feature.budget)
    implementation(projects.feature.save)
    implementation(projects.feature.finance)
    implementation(projects.feature.challenge)
    implementation(projects.feature.cost)

    implementation(projects.core.network)
}
