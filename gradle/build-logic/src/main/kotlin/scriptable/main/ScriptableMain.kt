package scriptable.main

import ScriptColor
import ScriptIcon
import SettingsScriptableEnd
import SettingsScriptableStart
import findScriptableExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.kotlin.dsl.*
import scriptable.ScriptableTaskGroup
import setScriptableProperties
import version
import java.io.File

/**
 *  Kotlin Main
 *
 * @author IvanEOD ( 10/5/2023 at 10:43 AM EST )
 */


class ScriptableMain : Plugin<Project> {
    override fun apply(target: Project): Unit = with (target) {

        allprojects {
            group = "com.detonateproductions.scriptable"
            version = "1.0.0"

            repositories {
                mavenCentral()

            }

        }

        tasks.withType<Wrapper> {
            gradleVersion = version("gradle")
        }

        extensions.create<ScriptableExtension>("scriptable", this)

        val initializeScriptableProject = tasks.create<InitializeScriptableProject>("initializeScriptableProject")

        gradle.beforeProject {
            initializeScriptableProject.initialize()
        }

        val buildAllScriptables by tasks.registering {
            group = ScriptableTaskGroup
            dependsOn(initializeScriptableProject)
            childProjects.forEach { (name, project) ->
                if (name == "scripts") {
                    project.childProjects.forEach { (childName, _) ->
                        dependsOn(":$name:$childName:build")
                    }
                }
            }
        }

    }
}

abstract class InitializeScriptableProject : DefaultTask() {

    init {
        group = ScriptableTaskGroup
        description = "Initialize Scriptable project and build scriptable modules."
    }

    @TaskAction
    fun initialize(): Unit = with (project) {
        val extension = findScriptableExtension()
        val scripts = extension.scripts.get()
        if (scripts.isEmpty()) {
            logger.warn("No scripts found, skipping scriptable initialization...")
            return
        }

        scripts.forEach { script ->
            val project = findProject(script.moduleName)
            if (project == null) buildScriptProject(script, extension)
            else updateScriptProject(project, script, extension)
        }
        val outputProjectNames = scripts.mapTo(HashSet()) { it.moduleName }
        updateSettingsScript(outputProjectNames)
    }

    private fun Project.updateSettingsScript(scripts: Set<String>) {
        val settingsFile = projectDir.resolve("settings.gradle.kts")
        val settingsLines = settingsFile.readLines()
        val before = settingsLines.takeWhile { it != SettingsScriptableStart }
        val includes = settingsLines.drop(before.size).takeWhile { it != SettingsScriptableEnd }
        val after = settingsLines.drop(before.size + includes.size)
        val newIncludes = scripts.map { "include(\":scripts:$it\")" }
        val newSettingsLines = before + SettingsScriptableStart + newIncludes + after
        settingsFile.writeText(newSettingsLines.joinToString("\n"))
    }

    private fun ScriptableExtension.getDefaults() : Triple<ScriptIcon, ScriptColor, Boolean> =
        Triple( defaultIcon.get().let { if (it == ScriptIcon.Default) ScriptIcon.Desktop else it },
            defaultColor.get().let { if (it == ScriptColor.Default) ScriptColor.DeepGray else it },
            defaultMinifyScripts.get())

    private fun updateScriptProject(
        project: Project,
        script: ScriptableMetadata,
        extension: ScriptableExtension,
    ) {
        val (defaultIcon, defaultColor, defaultMinify) = extension.getDefaults()
        val propertiesFile = project.projectDir.resolve("gradle.properties")
        if (!propertiesFile.exists()) propertiesFile.createNewFile()
        setScriptableProperties(propertiesFile, script, defaultIcon, defaultColor, defaultMinify)
    }

    private fun Project.buildScriptProject(
        script: ScriptableMetadata,
        extension: ScriptableExtension,
    ) {
        val scriptsDirectory = projectDir.resolve("scripts").apply { if (!exists()) mkdirs() }
        val scriptDirectory = scriptsDirectory.resolve(script.moduleName)
        if (!scriptDirectory.exists()) scriptDirectory.mkdirs()
        scriptDirectory.createBuildScript()
        val (defaultIcon, defaultColor, defaultMinify) = extension.getDefaults()
        val propertiesFile = scriptDirectory.resolve("gradle.properties")
        if (!propertiesFile.exists()) propertiesFile.createNewFile()
        setScriptableProperties(propertiesFile, script, defaultIcon, defaultColor, defaultMinify)
        val kotlinDirectory = scriptDirectory.resolve("src/jsMain/kotlin")
        if (!kotlinDirectory.exists()) kotlinDirectory.mkdirs()
        val directories= kotlinDirectory.listFiles() ?: emptyArray()
        if (directories.isEmpty()) {
            val main = kotlinDirectory.resolve("main.kt")
            main.createNewFile()
            main.writeText(scriptableScriptTemplate(script))
        }
    }

    private fun File.createBuildScript(): Boolean {
        val file = resolve("build.gradle.kts")
        return if (!file.exists()) {
            file.createNewFile()
            file.writeText(scriptableBuildScriptTemplate)
            true
        } else false
    }


    private fun scriptableScriptTemplate(meta: ScriptableMetadata) = """/**
 * ${meta.scriptableName}
 */
            
fun main() {
    println("Hello Scriptable, from Kotlin!")
}
"""

    companion object {
        private const val scriptableBuildScriptTemplate = "plugins {\n\tid(\"scriptable\")\n}"
    }

}
