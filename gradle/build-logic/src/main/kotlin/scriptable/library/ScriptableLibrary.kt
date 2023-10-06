package scriptable.library

import apply
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import scriptable.js.KotlinJsBase


/**
 *  Scriptable Library
 *
 * @author IvanEOD ( 10/5/2023 at 3:54 PM EST )
 */
class ScriptableLibrary : Plugin<Project> {
    override fun apply(target: Project): Unit = with (target) {
        pluginManager.apply<KotlinJsBase>()

        val kotlin = the<KotlinMultiplatformExtension>()
        kotlin.sourceSets.configureEach {
            dependencies {
                implementation(rootProject.project(":scriptable"))
            }
        }

    }
}


