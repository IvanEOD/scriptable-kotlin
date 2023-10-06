package scriptable


import org.gradle.api.DomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import scriptable.main.ScriptMeta
import java.io.File
import scriptable.main.ScriptableMain

/**
 *  Projects
 *
 * @author IvanEOD ( 10/5/2023 at 10:47 AM EST )
 */


fun Project.properties(name: String): String = providers.gradleProperty(name).get()
fun Project.version(name: String): String = properties("$name.version")
fun Project.environment(name: String): String = providers.environmentVariable(name).get()

internal const val SettingsScriptableStart = "// Scriptables Start"
internal const val SettingsScriptableEnd = "// Scriptables End"

internal const val ScriptableName = "scriptable.name"
internal const val ScriptableIcon = "scriptable.icon"
internal const val ScriptableColor = "scriptable.color"
private val scriptablePropertyNames = listOf(ScriptableName, ScriptableIcon, ScriptableColor)

internal inline val Project.jsModuleName: String
    get() = project.name
internal inline val Project.jsScriptableName: String
    get() = properties(ScriptableName)
internal inline val Project.jsScriptableIcon: String
    get() = properties(ScriptableIcon)
internal inline val Project.jsScriptableColor: String
    get() = properties(ScriptableColor)

private fun Project.findScriptableRoot(): Project =
    if (plugins.hasPlugin(ScriptableMain::class)) this
    else parent?.findScriptableRoot() ?: this

private inline val Project.rootBuildDirectory get() = findScriptableRoot().layout.buildDirectory.asFile.get()
internal fun Project.findModuleBuildDirectory() = rootBuildDirectory.resolve("js/packages/$jsModuleName/kotlin")
internal fun Project.findJsModuleFile() = findModuleBuildDirectory().resolve("$jsModuleName.js").takeIf { it.exists() }
internal fun Project.findCoreLibrary() = findModuleBuildDirectory().resolve("scriptable/library").apply { if (!exists()) mkdirs() }


private data class ScriptableProperties(
    val name: String,
    val icon: String,
    val color: String,
) {
    fun isMatch(name: String, value: String) = when (name) {
        ScriptableName -> this.name == value
        ScriptableIcon -> this.icon == value
        ScriptableColor -> this.color == value
        else -> false
    }

    fun update(name: String, value: String): ScriptableProperties = if (isMatch(name, value)) this else
        when (name) {
            ScriptableName -> copy(name = value)
            ScriptableIcon -> copy(icon = value)
            ScriptableColor -> copy(color = value)
            else -> this
        }

    fun writeTo(file: File) {
        val lines = file.readLines()
        val cleaned = lines.filter { line -> scriptablePropertyNames.none { line.startsWith(it) } }.dropLastWhile { it.isEmpty() }
        val newLines = cleaned + listOf(
            "$ScriptableName=$name",
            "$ScriptableIcon=$icon",
            "$ScriptableColor=$color",
        )
        file.writeText(newLines.joinToString("\n"))
    }

    companion object {

        fun fromFile(file: File): ScriptableProperties {
            val lines = file.readLines()
            val name = lines.find { it.startsWith(ScriptableName) }?.substringAfter("=") ?: ""
            val icon = lines.find { it.startsWith(ScriptableIcon) }?.substringAfter("=") ?: ""
            val color = lines.find { it.startsWith(ScriptableColor) }?.substringAfter("=") ?: ""
            return ScriptableProperties(name, icon, color)
        }


    }
}

internal fun Project.setScriptableProperties(
    meta: ScriptMeta,
    defaultIcon: ScriptIcon,
    defaultColor: ScriptColor,
) {
    val icon = if (meta.icon.isDefault()) defaultIcon.value else meta.icon.value
    val color = if (meta.color.isDefault()) defaultColor.value else meta.color.value
    val properties = ScriptableProperties(meta.scriptableName, icon, color)
    setScriptableProperties(properties)
}

private fun Project.setScriptableProperties(
    properties: ScriptableProperties,
) {
    val propertiesFile = getGradlePropertiesFile()
    val scriptableProperties = ScriptableProperties.fromFile(propertiesFile)
    if (properties != scriptableProperties) properties.writeTo(propertiesFile)
}

internal fun Project.setScriptableProperty(
    name: String,
    value: String,
) {
    val propertiesFile = getGradlePropertiesFile()
    val scriptableProperties = ScriptableProperties.fromFile(propertiesFile)
    val updatedScriptableProperties = scriptableProperties.update(name, value)
    if (scriptableProperties != updatedScriptableProperties) updatedScriptableProperties.writeTo(propertiesFile)
}


private fun Project.getGradlePropertiesFile(): File = projectDir.resolve("gradle.properties")
    .apply { if (!exists()) createNewFile() }



internal fun Project.ext(
    propertyName: String,
    value: Boolean,
) {
    ext(propertyName, value.toString())
}

internal fun Project.ext(
    propertyName: String,
    value: String,
) {
    extensions.extraProperties[propertyName] = value
}

internal inline fun <reified S : Any> DomainObjectCollection<in S>.configureEach(
    noinline action: S.() -> Unit,
) {
    withType().configureEach(action)
}

internal inline fun <reified S : Task> DomainObjectCollection<in S>.disable() {
    configureEach<S> {
        enabled = false
    }
}

internal inline fun <reified S : Task> DomainObjectCollection<in S>.disable(
    noinline predicate: S.() -> Boolean,
) {
    configureEach<S> {
        if (predicate()) {
            enabled = false
        }
    }
}

internal fun Project.disableTestsWithoutSources() {
    afterEvaluate {
        val sourceSet = SourceSet.values()
            .single { it.taskNames.mapNotNull(tasks::findByPath).isNotEmpty() }

        sourceSet.taskNames
            .mapNotNull(tasks::findByPath)
            .forEach { task ->
                task.onlyIf {
                    val kotlin = project.extensions.getByName<KotlinProjectExtension>("kotlin")
                    sourceSet.names
                        .asSequence()
                        .map { kotlin.sourceSets.getByName(it) }
                        .flatMap { it.kotlin.sourceDirectories }
                        .any { it.exists() }
                }
            }
    }
}

private enum class SourceSet(
    val names: Set<String>,
    val taskPrefixes: Set<String>,
) {
    MULTIPLATFORM(
        names = setOf("jsTest", "commonTest"),
        taskPrefixes = setOf("jsTest", "jsIrTest", "jsLegacyTest")
    ),

    JS(
        names = setOf("test"),
        taskPrefixes = setOf("test", "irTest", "legacyTest")
    ),
    ;

    val taskNames = taskPrefixes.map { "${it}PackageJson" }
}
