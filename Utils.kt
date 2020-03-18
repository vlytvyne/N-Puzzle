import java.lang.Exception
import kotlin.system.exitProcess

fun validate(errorMsg: String, runnable: () -> Unit) {
	try {
		runnable()
	} catch (e: Exception) {
		println(errorMsg)
		exitProcess(1)
	}
}

fun invalidExit(errorMsg: String) {
	println(errorMsg)
	exitProcess(1)
}
