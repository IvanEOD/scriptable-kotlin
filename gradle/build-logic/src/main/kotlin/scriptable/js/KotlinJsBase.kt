package scriptable.js

import configureEach
import disableTestsWithoutSources
import ext
import jsModuleName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import scriptable.*
import scriptable.JsCompiler
import scriptable.KotlinMultiplatform
import scriptable.NoWarn
import scriptable.OutputGranularity


/**
 *  Kotlin Js Base
 *
 * @author IvanEOD ( 10/5/2023 at 3:48 PM EST )
 */
class KotlinJsBase : Plugin<Project> {
    override fun apply(target: Project): Unit = with (target) {
        ext(NoWarn, true)
        ext(JsCompiler, "ir")
        ext(DomApiIncluded, false)
        ext(OutputGranularity, "per-module")
        plugins.apply(KotlinMultiplatform)
        disableTestsWithoutSources()
        plugins.apply("idea")
        val ideaModule = the<IdeaModel>()
        with (ideaModule.module) {
            sourceDirs = sourceDirs + file("build/generated/scriptable/main/kotlin")
            generatedSourceDirs = generatedSourceDirs + file("build/generated/scriptable/main/kotlin")
        }

        val kotlin = the<KotlinMultiplatformExtension>()
        kotlin.js(KotlinJsCompilerType.IR) {
            moduleName = jsModuleName
            useCommonJs()
            generateTypeScriptDefinitions()
            nodejs()
            binaries.executable()
        }

        projectDir.resolve("src/jsMain/kotlin").apply { if (!exists()) mkdirs() }

        tasks.configureEach<KotlinJsCompile> {
            kotlinOptions {
                freeCompilerArgs += listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlin.js.ExperimentalJsExport",
                    "-opt-in=kotlin.contracts.ExperimentalContracts",
                    "-Xgenerate-polyfills=false",
                )
            }
        }

    }

}



