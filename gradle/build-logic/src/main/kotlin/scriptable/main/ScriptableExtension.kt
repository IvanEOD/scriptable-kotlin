package scriptable.main

import ScriptColor
import ScriptIcon
import environment
import net.pearx.kasechange.toKebabCase
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty
import properties
import javax.inject.Inject


/**
 *  Scriptable Extension
 *
 * @author IvanEOD ( 10/5/2023 at 3:09 PM EST )
 */
abstract class ScriptableExtension @Inject constructor(
    private val project: Project
) {
    internal val scripts: SetProperty<ScriptableMetadata> = project.objects.setProperty<ScriptableMetadata>().convention(emptySet())
    val iCloudScriptableDirectory: DirectoryProperty = project.objects.directoryProperty()
    val iCloudScriptableCacheDirectory: DirectoryProperty = project.objects.directoryProperty()

    operator fun get(project: Project): ScriptableMetadata = scripts.get().first { it.moduleName == project.name }

    val defaultMinifyScripts: Property<Boolean> = project.objects.property<Boolean>().convention(true)

    val defaultIcon: Property<ScriptIcon> = project.objects.property<ScriptIcon>().convention(ScriptIcon.Desktop)
    val defaultColor: Property<ScriptColor> = project.objects.property<ScriptColor>().convention(ScriptColor.DeepGray)

    fun environment(name: String): String = project.environment(name)
    fun properties(name: String): String = project.properties(name)

    fun include(
        name: String,
        icon: Any = ScriptIcon.Default,
        color: Any = ScriptColor.Default,
        minify: Boolean? = null,
    ) = include(name, name.toKebabCase(), icon, color, minify)

    fun include(
        name: String,
        moduleName: String,
        icon: Any = ScriptIcon.Default,
        color: Any = ScriptColor.Default,
        minify: Boolean? = null
    ) {
        scripts.add(resolveInclude(moduleName, name, icon, color, minify))
    }

    private fun resolveInclude(
        moduleName: String,
        scriptableName: String,
        icon: Any = ScriptIcon.Default,
        color: Any = ScriptColor.Default,
        minify: Boolean? = null
    ) : ScriptableMetadata {
        val resolvedIcon = resolveIcon(scriptableName, icon)
        val resolvedColor = resolveColor(scriptableName, color)
        return ScriptableMetadata(moduleName, scriptableName, resolvedIcon, resolvedColor, minify)
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