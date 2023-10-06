import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType

/**
 *  Kotlin Js Default
 *
 * @author IvanEOD ( 10/5/2023 at 12:20 PM EST )
 */

internal const val NoWarn = "kotlin.mpp.stability.nowarn"
internal const val JsCompiler = "kotlin.js.compiler"
internal const val DomApiIncluded = "kotlin.js.stdlib.dom.api.included"
internal const val OutputGranularity = "kotlin.js.ir.output.granularity"
internal const val KotlinMultiplatform = "org.jetbrains.kotlin.multiplatform"
internal const val JsMainImplementation = "jsMainImplementation"


internal fun Project.applyKotlinJsDefault() {

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

    val build = tasks.named("build")

    val generateDeclarations by tasks.registering {
        dependsOn(build)
        doLast {
            val buildDirectory = findModuleBuildDirectory()
            exec {
                workingDir(buildDirectory)
                commandLine("cmd", "/c", "dukat", "-d", "declarations", "${jsModuleName}.d.ts")
            }
            val declarationDirectory = buildDirectory.resolve("declarations")
            val declarationFile = declarationDirectory.listFiles()?.firstOrNull { it.name.startsWith(jsModuleName) }
            if (declarationFile != null) {
                val declarationText = declarationFile.readText()
                val moduleLine = "@file:JsModule(\"$jsScriptableName\")"
                val coreLibrary = findCoreLibrary()
                try {
                    val libraryDeclaration = coreLibrary.resolve("$jsScriptableName.kt")
                    if (!libraryDeclaration.exists()) libraryDeclaration.createNewFile()
                    libraryDeclaration.writeText("$moduleLine\n$declarationText")
                } catch (e: Exception) {
                    println("Failed to write library declaration: ${e.message}")
                    throw e
                }
            }
        }
    }

    task(":build").finalizedBy(generateDeclarations)

    tasks.configureEach<KotlinJsCompile> {
        kotlinOptions {
            freeCompilerArgs += listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.js.ExperimentalJsExport",
                "-Xgenerate-polyfills=false",
            )
        }
    }


}


