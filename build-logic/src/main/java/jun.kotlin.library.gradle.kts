import jun.money.mate.configureHiltKotlin
import jun.money.mate.configureKotest
import jun.money.mate.configureKotlin
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}

configureKotlin()
configureKotest()
configureHiltKotlin()