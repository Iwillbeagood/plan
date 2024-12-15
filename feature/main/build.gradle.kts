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
    implementation(projects.feature.spendingPlan)
    implementation(projects.feature.comsumptionSpend)
    implementation(projects.feature.save)

    implementation(projects.core.network)
}