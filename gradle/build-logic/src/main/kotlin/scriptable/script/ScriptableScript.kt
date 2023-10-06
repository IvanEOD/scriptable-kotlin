package scriptable.script

import apply
import findModuleBuildDirectory
import findModulePackageDirectory
import findRootPackagesDirectory
import findScriptMetadata
import findScriptableExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import scriptable.ScriptableTaskGroup
import scriptable.js.KotlinJsBase
import scriptable.main.ScriptableExtension
import scriptable.main.ScriptableMetadata


/**
 *  Scriptable Script
 *
 * @author IvanEOD ( 10/5/2023 at 3:42 PM EST )
 */
class ScriptableScript : Plugin<Project> {
    override fun apply(target: Project): Unit = with (target) {
        pluginManager.apply<KotlinJsBase>()

        val kotlin = the<KotlinMultiplatformExtension>()
        kotlin.sourceSets.configureEach {
            dependencies {
                implementation(rootProject.project(":scriptable"))
                implementation(rootProject.project(":library"))
            }
        }

        val packageScriptable = tasks.register<PackageScriptable>("packageScriptable")
        val syncToCloud = tasks.register<SyncToCloud>("syncToCloud")

        packageScriptable {
            inputFile = findModulePackageDirectory()
        }

        syncToCloud {
            inputFile = packageScriptable.flatMap { it.outputFile }
            outputFile = findScriptableExtension().let { extension ->
                extension.iCloudScriptableDirectory.map { it.file("${extension[project].scriptableName}.js") }
            }
        }

        tasks.named("build").configure {
            dependsOn(packageScriptable)
            dependsOn(syncToCloud)
        }

    }
}

abstract class PackageScriptable : DefaultTask() {

    init {
        group = ScriptableTaskGroup
        description = "Package Scriptable module into it's own source file for Scriptable."
    }

    private val extension: ScriptableExtension by lazy { project.findScriptableExtension() }
    private val rootPackagesDirectory by lazy { project.findRootPackagesDirectory() }

    @get:Input
    val shouldMinify: Property<Boolean> = project.objects.property(Boolean::class.java)
        .convention( project.provider { getShouldMinifyConvention() })

    @get:InputDirectory
    val inputFile: DirectoryProperty = project.objects.directoryProperty()
        .convention(inputFileConvention())

    @get:OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()
        .convention(outputFileConvention())

    @TaskAction
    fun packageScript(): Unit = with (project) {
        val script = findScriptMetadata(extension)
        if (script == null) {
            logger.warn("No script metadata found, skipping scriptable packaging...")
            return
        }

        val inputPath = inputFile.get().asFile.toRelativeString(rootPackagesDirectory.get().asFile)
        val outfilePath = outputFile.get().asFile.toRelativeString(rootPackagesDirectory.get().asFile)
        val minify = shouldMinify.get()
        val arguments = mutableListOf(
            "cmd",
            "/c",
            "esbuild",
            inputPath,
            "--bundle",
            "--outfile=$outfilePath",
            "--format=cjs"
        )
        if (minify) arguments.add("--minify")

        exec {
            workingDir(rootPackagesDirectory.get().asFile)
            commandLine(*arguments.toTypedArray())
        }

    }

    private fun getShouldMinifyConvention() = project.findScriptableExtension().let { extension ->
        val script = project.findScriptMetadata(extension)
        script?.minify ?: extension.defaultMinifyScripts.get()
    }
    private fun inputFileConvention() = rootPackagesDirectory.map { it.dir("./${project.name}") }
    private fun outputFileConvention() = rootPackagesDirectory.map { it.file("./${project.name}/out.js") }

}

abstract class SyncToCloud : DefaultTask() {

    init {
        group = ScriptableTaskGroup
        description = "Sync Scriptable module to iCloud Drive."
    }

    @get:InputFile
    abstract val inputFile: RegularFileProperty
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun sync() {
        val extension = project.findScriptableExtension()
        val script = project.findScriptMetadata(extension)
        if (script == null) {
            logger.warn("No script metadata found for [${project.name}]. Skipping scriptable sync.")
            return
        }
        logger.info("Updating ${script.moduleName}...")
        val inputText = inputFile.get().asFile.readText()
        val header = buildScriptableHeader(script)
        outputFile.get().asFile.apply { if (!exists()) createNewFile() }
            .writeText("$header\n$inputText")

    }


    companion object {
        private fun buildScriptableHeader(meta: ScriptableMetadata): String {
            return """
                // Variables used by Scriptable.
                // These must be at the very top of the file. Do not edit.
                // icon-color: ${meta.color}; icon-glyph: ${meta.icon};
            """.trimIndent()
        }
    }

}
