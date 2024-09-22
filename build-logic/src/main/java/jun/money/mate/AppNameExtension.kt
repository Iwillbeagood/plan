package jun.money.mate

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "jun.money.mate.$name"
    }
}