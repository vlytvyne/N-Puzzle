import java.lang.Exception
import kotlin.system.exitProcess

fun validate(errorMsg: String, runnable: () -> Unit) {
	try {
		runnable()
	} catch (e: Exception) {
		invalidExit(errorMsg)
	}
}

fun invalidExit(errorMsg: String) {
	println(errorMsg.fontColor(AnsiColor.RED))
	exitProcess(1)
}

val Int.isEven: Boolean
	get() = this % 2 == 0

val Int.isOdd: Boolean
	get() = this % 2 != 0