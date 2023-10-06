package scriptable

/**
 *  Script Color
 *
 * @author IvanEOD ( 10/5/2023 at 11:10 AM EST )
 */
enum class ScriptColor(val value: String, val red: Int, val green: Int, val blue: Int) {
    Default("default", 0, 0, 0),

    Red("red", 218, 78, 62),
    Pink("pink", 218, 64, 108),
    Purple("purple", 156, 55, 186),
    DeepPurple("deep-purple", 110, 71, 191),
    DeepBlue("deep-blue", 83, 95, 195),
    Blue("blue", 81, 148, 230),
    Cyan("cyan", 92, 184, 207),
    Teal("teal", 86, 166, 155),
    DeepGreen("deep-green", 109, 186, 94),
    Green("green", 158, 196, 101),
    Yellow("yellow", 233, 190, 82),
    Orange("orange", 231, 154, 64),
    LightBrown("light-brown", 168, 112, 78),
    Brown("brown", 143, 83, 55),
    DeepBrown("deep-brown", 108, 70, 39),
    LightGray("light-gray", 133, 134, 144),
    Gray("gray", 106, 109, 120),
    DeepGray("deep-gray", 70, 76, 82);

    fun isDefault() = this == Default


    companion object {
        private val enumNames by lazy { values().map { it.name } }
        private val enumValues by lazy { values().map { it.value } }
        fun valueOrNull(input: Any): ScriptColor? = when (input) {
            is ScriptColor -> input
            is String -> fromStringOrNull(input)
            else -> null
        }
        private fun fromStringOrNull(input: String): ScriptColor? = when (input) {
            in enumNames -> valueOf(input)
            in enumValues -> values().first { it.value == input }
            else -> null
        }

    }

}
