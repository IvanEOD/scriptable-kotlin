package scriptable.main

import ScriptColor
import ScriptIcon


/**
 *  Scriptable Metadata
 *
 * @author IvanEOD ( 10/5/2023 at 3:12 PM EST )
 */
data class ScriptableMetadata(
    val moduleName: String,
    val scriptableName: String,
    val icon: ScriptIcon,
    val color: ScriptColor,
    val minify: Boolean? = null,
)
