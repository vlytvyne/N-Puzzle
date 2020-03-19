const val ESCAPE = '\u001B'
const val RESET = "$ESCAPE[0m"

fun String.fontColor(color: AnsiColor) =
	"${color.foreground}$this$RESET"

fun String.backgroundColor(color: AnsiColor) =
	"${color.background}$this$RESET"

enum class AnsiColor(baseCode: Int) {
	BLACK(30),
	RED(31),
	GREEN(32),
	YELLOW(33),
	BLUE(34),
	MAGENTA(35),
	CYAN(36),
	LIGHT_GRAY(37),

	DARK_GRAY(90),
	LIGHT_RED(91),
	LIGHT_GREEN(92),
	LIGHT_YELLOW(93),
	LIGHT_BLUE(94),
	LIGHT_MAGENTA(95),
	LIGHT_CYAN(96),
	WHITE(97);

	/** ANSI modifier string to apply the color to the text itself */
	val foreground: String = "$ESCAPE[${baseCode}m"

	/** ANSI modifier string to apply the color the text's background */
	val background: String = "$ESCAPE[${baseCode + 10}m"
}