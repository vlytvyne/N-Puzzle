import kotlin.system.exitProcess

fun invalidExit(errorMsg: String) {
	println(errorMsg.fontColor(AnsiColor.RED))
	exitProcess(1)
}

fun showHelp() {
	println("Programm is used to solve n-puzzle in snail configuration")
	println("  -h  - help")
	println("  -v  - verbose output")
	println("  -hr - heuristic function")
	println()
	println("Heuristic functions:")
	println("  1 - 'hamming' - count number of misplaced tiles")
	println("  2 - 'manhattan' - count sum of manhattan distances of tiles final positions")
	println("  3 - 'lin-conf' - manhattan distance plus number of linear conflicts")
	exitProcess(0)
}

val Int.isEven: Boolean
	get() = this % 2 == 0

val Int.isOdd: Boolean
	get() = this % 2 != 0