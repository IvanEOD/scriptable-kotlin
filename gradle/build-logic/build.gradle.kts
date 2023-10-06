import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(kotlin("gradle-plugin"))
    implementation(kotlin("gradle-plugin-api"))
    implementation("org.yaml:snakeyaml:2.2")
    implementation("net.pearx.kasechange:kasechange:1.4.1")
}

gradlePlugin {
    plugins {
        create("scriptable-main") {
            id = "scriptable-main"
            implementationClass = "scriptable.main.ScriptableMain"
        }
        create("scriptable") {
            id = "scriptable"
            implementationClass = "scriptable.script.ScriptableScript"
        }
        create("kotlin-js-base") {
            id = "kotlin-js-base"
            implementationClass = "scriptable.js.KotlinJsBase"
        }
        create("scriptable-library") {
            id = "scriptable-library"
            implementationClass = "scriptable.library.ScriptableLibrary"
        }
    }
}