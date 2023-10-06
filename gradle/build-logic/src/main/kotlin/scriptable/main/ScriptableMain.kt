package scriptable.main

import ScriptColor
import ScriptIcon
import net.pearx.kasechange.toKebabCase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.setProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.the
import SettingsScriptableEnd
import SettingsScriptableStart
import environment
import properties
import setScriptableProperties
import java.io.File
import javax.inject.Inject

/**
 *  Kotlin Main
 *
 * @author IvanEOD ( 10/5/2023 at 10:43 AM EST )
 */


class ScriptableMain : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyKotlinMain()
    }
}



data class ScriptMeta(
    val moduleName: String,
    val scriptableName: String,
    val icon: ScriptIcon,
    val color: ScriptColor,
)

abstract class ScriptableExtension @Inject constructor(
    private val project: Project
) {
    internal val scripts: SetProperty<ScriptMeta> = project.objects.setProperty<ScriptMeta>().convention(emptySet())
    val iCloudScriptableDirectory: DirectoryProperty = project.objects.directoryProperty()
    val iCloudScriptableCacheDirectory: DirectoryProperty = project.objects.directoryProperty()

    val defaultIcon: Property<ScriptIcon> = project.objects.property<ScriptIcon>().convention(ScriptIcon.Desktop)
    val defaultColor: Property<ScriptColor> = project.objects.property<ScriptColor>().convention(ScriptColor.DeepGray)

    fun environment(name: String): String = project.environment(name)
    fun properties(name: String): String = project.properties(name)

    fun include(
        name: String,
        icon: Any = ScriptIcon.Default,
        color: Any = ScriptColor.Default,
    ) = include(name, name.toKebabCase(), icon, color)

    fun include(
        name: String,
        moduleName: String,
        icon: Any = ScriptIcon.Default,
        color: Any = ScriptColor.Default,
    ) {
        scripts.add(resolveInclude(moduleName, name, icon, color))
    }

    private fun resolveInclude(
        moduleName: String,
        scriptableName: String,
        icon: Any = ScriptIcon.Default,
        color: Any = ScriptColor.Default,
    ) : ScriptMeta {
        println("Scriptable: $scriptableName")
        val resolvedIcon = resolveIcon(scriptableName, icon)
        val resolvedColor = resolveColor(scriptableName, color)
        return ScriptMeta(moduleName, scriptableName, resolvedIcon, resolvedColor)
    }

    private fun resolveIcon(
        name: String,
        icon: Any,
    ): ScriptIcon {
        val result = ScriptIcon.valueOrNull(icon)
        if (result == null) project.logger.warn("Scriptable Warning:\n\tScriptable:$name\n\tIcon $icon not found, Check the ScriptIcon enum for available icons... using the project default.")
        return result ?: ScriptIcon.Default
    }

    private fun resolveColor(
        name: String,
        color: Any,
    ): ScriptColor {
        val result = ScriptColor.valueOrNull(color)
        if (result == null) project.logger.warn("Scriptable Warning:\n\tScriptable:$name\n\tColor $color not found, Check the ScriptColor enum for available colors... using the project default.")
        return result ?: ScriptColor.Default
    }

}

internal fun Project.applyKotlinMain() {
    extensions.create<ScriptableExtension>("scriptable", this)
    val scriptableExtension = the<ScriptableExtension>()
    val scripts = scriptableExtension.scripts.get()
    if (scripts.isEmpty()) println("No scripts found, skipping scriptable build...")
    else println("Found ${scripts.size} scripts, building scriptable project...")
    scripts.forEach { script ->
        val project = findProject(script.moduleName)
        if (project == null) buildScriptProject(script, scriptableExtension)
        else updateScriptProject(project, script, scriptableExtension)
    }
    updateBuildSettings(scripts.map { it.moduleName }.toSet())
}



private fun Project.updateBuildSettings(
    scripts: Set<String>
) {
    val settingsFile = projectDir.resolve("settings.gradle.kts")
    val settingsLines = settingsFile.readLines()
    val before = settingsLines.takeWhile { it != SettingsScriptableStart }
    val includes = settingsLines.drop(before.size).takeWhile { it != SettingsScriptableEnd }
    val after = settingsLines.drop(before.size + includes.size)
    val newIncludes = scripts.map { "include(\":script:$it\")" }
    val newSettingsLines = before + SettingsScriptableStart + newIncludes + after
    settingsFile.writeText(newSettingsLines.joinToString("\n"))
}

private fun ScriptableExtension.getDefaults() : Pair<ScriptIcon, ScriptColor> =
    defaultIcon.get().let { if (it == ScriptIcon.Default) ScriptIcon.Desktop else it } to
    defaultColor.get().let { if (it == ScriptColor.Default) ScriptColor.DeepGray else it }

private fun Project.updateScriptProject(
    project: Project,
    script: ScriptMeta,
    extension: ScriptableExtension,
) {
    val (defaultIcon, defaultColor) = extension.getDefaults()
    project.setScriptableProperties(script, defaultIcon, defaultColor)
}

private fun Project.buildScriptProject(
    script: ScriptMeta,
    extension: ScriptableExtension,
) {
    val scriptsDirectory = projectDir.resolve("scripts")
    val scriptDirectory = scriptsDirectory.resolve(script.moduleName)
    if (!scriptDirectory.exists()) scriptDirectory.mkdirs()
    scriptDirectory.createBuildScript()
    val (defaultIcon, defaultColor) = extension.getDefaults()
    setScriptableProperties(script, defaultIcon, defaultColor)
}

private fun File.createBuildScript() {
    val file = resolve("build.gradle.kts")
    if (!file.exists()) {
        file.createNewFile()
        file.writeText(scriptableBuildScriptTemplate)
    }
}

private const val scriptableBuildScriptTemplate = """
    plugins {
        id("scriptable-kotlin")
    }
"""