rootProject.name = "scriptable-kotlin"

pluginManagement {
    includeBuild("./gradle/build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        kotlin("multiplatform") version kotlinVersion

    }

}

include("library")
include("scriptable")

// Scriptables Start
include(":scripts:show-table-example")
include(":scripts:show-alert-example")
// Scriptables End