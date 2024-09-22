import jun.money.mate.configureHiltAndroid
import jun.money.mate.configureKotestAndroid
import jun.money.mate.configureKotlinAndroid


plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
