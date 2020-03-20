import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.system.measureTimeMillis
import AnsiColor.*
import java.lang.Exception

val comparator = Comparator<State> {
	state1, state2 ->
	if (state1.f == state2.f) {
		return@Comparator state1.h - state2.h
	}
	return@Comparator state1.f - state2.f
}

val openList = PriorityQueue<State>(comparator)
val closedList = arrayListOf<State>()

private var timeComplexity: Long = 0
private var sizeComplexity: Long = 0
private var turns = 0
private var time: Long = 0

fun main(args: Array<String>) {
	CommandLineConfig.parseCommandLineArguments(args)

	if (CommandLineConfig.helpCalled) {
		showHelp()
	}
	Board.heuristic = CommandLineConfig.heuristic
	val startBoard = parseInput()
	if (!startBoard.isSolvable) {
		invalidExit("This puzzle is unsolvable")
	}
	time = measureTimeMillis { solvePuzzle(startBoard) }
	printSolveDetails()
}

//https://uk.wikipedia.org/wiki/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_%D0%BF%D0%BE%D1%88%D1%83%D0%BA%D1%83_A*
fun solvePuzzle(startBoard: Board) {
	val startState = State(startBoard)
	openList.add(startState)

	while (openList.isNotEmpty()) {
		sizeComplexity = max(sizeComplexity, (openList.size + closedList.size).toLong())
		timeComplexity++
		val currentState = openList.poll()

		if (currentState.isSolved) {
			currentState.printTrace()
			turns = currentState.g
			break
		}
		expandState(currentState)
		closedList.add(currentState)
	}
}


private fun expandState(state: State) {
	val states = state.getNeighbourStates()
	states.filter { !closedList.contains(it) }.forEach { openList.add(it) }
}

private fun parseInput(): Board {
	val scanner = Scanner(System.`in`)
	var size: Int? = null
	val boardLines = ArrayList<String>()

	while (scanner.hasNextLine()) {
		var line = scanner.nextLine()
		line = eraseComment(line)
		if (line.isBlank()) {
			continue
		}
		if (size == null) {
			size = extractSize(line)
			if (size < 3) {
				invalidExit("N-puzzle can\'t be smaller than 3x3")
			}
		} else {
			boardLines.add(line)
		}
	}
	if (size != null) {
		Board.setBoardsSize(size)
	} else {
		invalidExit("Input is invalid")
	}
	return Board.createBoard(boardLines)
}

private fun extractSize(line: String): Int {
	try {
		return line.trim().toInt()
	} catch (e: Exception) {
		invalidExit("Invalid size input")
	}
	return 0
}

private fun eraseComment(line: String): String {
	if (line.contains('#')) {
		val commentLength = line.length - line.split('#')[0].length
		return line.dropLast(commentLength)
	}
	return line
}

private fun printSolveDetails() {
	println("---------------------".fontColor(BLUE))
	println("SOLVED".fontColor(GREEN))
	println("turns: $turns".fontColor(BLUE))
	println("time: $time ms".fontColor(RED))
	println("time complexity: $timeComplexity".fontColor(YELLOW))
	println("size complexity: $sizeComplexity".fontColor(CYAN))
	println("heuristic: ${Board.heuristic}".fontColor(MAGENTA))
}