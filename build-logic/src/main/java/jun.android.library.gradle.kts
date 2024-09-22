import jun.money.mate.configureCoroutineAndroid
import jun.money.mate.configureHiltAndroid
import jun.money.mate.configureKotest
import jun.money.mate.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureKotest()
configureCoroutineAndroid()
configureHiltAndroid()