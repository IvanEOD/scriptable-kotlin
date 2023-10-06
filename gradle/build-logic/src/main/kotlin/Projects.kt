import org.gradle.api.DomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import scriptable.ScriptableColor
import scriptable.ScriptableIcon
import scriptable.ScriptableMinify
import scriptable.ScriptableName
import scriptable.main.ScriptableExtension
import scriptable.main.ScriptableMetadata
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


private val scriptablePropertyNames = listOf(ScriptableName, ScriptableIcon, ScriptableColor, ScriptableMinify)

internal inline val Project.jsModuleName: String
    get() = project.name
internal inline val Project.jsScriptableName: String
    get() = properties(ScriptableName)
internal inline val Project.jsScriptableIcon: String
    get() = properties(ScriptableIcon)
internal inline val Project.jsScriptableColor: String
    get() = properties(ScriptableColor)
internal inline val Project.jsScriptableMinify: Boolean
    get() = properties(ScriptableMinify).toBoolean()

internal fun Project.findScriptableRoot(): Project =
    if (plugins.hasPlugin(ScriptableMain::class)) this
    else parent?.findScriptableRoot() ?: this

internal fun Project.findScriptableExtension(): ScriptableExtension =
    findScriptableRoot().the<ScriptableExtension>()

internal fun Project.findScriptMetadata(extension: ScriptableExtension) =
    extension.scripts.get().find { it.moduleName == jsModuleName }

internal inline fun <reified T> PluginManager.apply() = apply(T::class.java)

private inline val Project.rootBuildDirectory get() = findScriptableRoot().layout.buildDirectory
internal fun Project.findRootPackagesDirectory() = rootBuildDirectory.dir("js/packages")
internal fun Project.findModulePackageDirectory() = rootBuildDirectory.dir("js/packages/$jsModuleName")
internal fun Project.findModuleBuildDirectory() = rootBuildDirectory.dir("js/packages/$jsModuleName/kotlin")
internal fun Project.findJsModuleFile() = rootBuildDirectory.file("js/packages/$jsModuleName/kotlin/$jsModuleName.js")
internal fun Project.findScriptableLibrary() = rootBuildDirectory.dir("scriptable/library")
internal fun Project.findScriptableCache() = rootBuildDirectory.dir("scriptable/cache")

private data class ScriptableProperties(
    val name: String,
    val icon: String,
    val color: String,
    val minify: String
) {
    fun isMatch(name: String, value: String) = when (name) {
        ScriptableName -> this.name == value
        ScriptableIcon -> this.icon == value
        ScriptableColor -> this.color == value
        ScriptableMinify -> this.minify == value
        else -> false
    }

    fun update(name: String, value: String): ScriptableProperties = if (isMatch(name, value)) this else
        when (name) {
            ScriptableName -> copy(name = value)
            ScriptableIcon -> copy(icon = value)
            ScriptableColor -> copy(color = value)
            ScriptableMinify -> copy(minify = value)
            else -> this
        }

    fun writeTo(file: File) {
        val lines = file.readLines()
        val cleaned = lines.filter { line -> scriptablePropertyNames.none { line.startsWith(it) } }.dropLastWhile { it.isEmpty() }
        val newLines = cleaned + listOf(
            "$ScriptableName=$name",
            "$ScriptableIcon=$icon",
            "$ScriptableColor=$color",
            "$ScriptableMinify=$minify",
        )
        file.writeText(newLines.joinToString("\n"))
    }

    companion object {

        fun fromFile(file: File): ScriptableProperties {
            val lines = file.readLines()
            val name = lines.find { it.startsWith(ScriptableName) }?.substringAfter("=") ?: ""
            val icon = lines.find { it.startsWith(ScriptableIcon) }?.substringAfter("=") ?: ""
            val color = lines.find { it.startsWith(ScriptableColor) }?.substringAfter("=") ?: ""
            val minify = lines.find { it.startsWith(ScriptableMinify) }?.substringAfter("=") ?: ""
            return ScriptableProperties(name, icon, color, minify)
        }


    }
}

private fun scriptableProperties(
    meta: ScriptableMetadata,
    defaultIcon: ScriptIcon,
    defaultColor: ScriptColor,
    defaultMinify: Boolean
): ScriptableProperties {
    val icon = if (meta.icon.isDefault()) defaultIcon.value else meta.icon.value
    val color = if (meta.color.isDefault()) defaultColor.value else meta.color.value
    val minify = (meta.minify ?: defaultMinify).toString()
    return ScriptableProperties(meta.scriptableName, icon, color, minify)
}

internal fun Project.setScriptableProperties(
    meta: ScriptableMetadata,
    defaultIcon: ScriptIcon,
    defaultColor: ScriptColor,
    defaultMinify: Boolean,
): Boolean = setScriptableProperties(scriptableProperties(meta, defaultIcon, defaultColor, defaultMinify))

internal fun setScriptableProperties(
    file: File,
    meta: ScriptableMetadata,
    defaultIcon: ScriptIcon,
    defaultColor: ScriptColor,
    defaultMinify: Boolean,
): Boolean = setScriptableProperties(scriptableProperties(meta, defaultIcon, defaultColor, defaultMinify), file)


private fun Project.setScriptableProperties(
    properties: ScriptableProperties,
): Boolean = setScriptableProperties(properties, getGradlePropertiesFile())

private fun setScriptableProperties(
    properties: ScriptableProperties,
    propertiesFile: File,
): Boolean = if (properties != ScriptableProperties.fromFile(propertiesFile)) {
    properties.writeTo(propertiesFile)
    true
} else false

internal fun Project.setScriptableProperty(
    name: String,
    value: String,
) {
    val propertiesFile = getGradlePropertiesFile()
    val scriptableProperties = ScriptableProperties.fromFile(propertiesFile)
    val updatedScriptableProperties = scriptableProperties.update(name, value)
    if (scriptableProperties != updatedScriptableProperties) updatedScriptableProperties.writeTo(propertiesFile)
}


private fun Project.getGradlePropertiesFile(): File = projectDir
    .resolve("gradle.properties")
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
