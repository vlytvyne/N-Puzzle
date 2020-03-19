import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.system.measureTimeMillis
import AnsiColor.*

val comparator = Comparator<State> {
	state1, state2 ->
	if (state1.f == state2.f) {
		return@Comparator state1.h - state2.h
	}
	return@Comparator state1.f - state2.f
}

val openList = PriorityQueue<State>(comparator)
val closedList = arrayListOf<State>()

var timeComplexity: Long = 0
var sizeComplexity: Long = 0

fun main() {
	Board.heuristic = Heuristic.MANHATTAN
	val startBoard = parseInput()
	startBoard.print()
	if (!startBoard.isSolvable) {
		invalidExit("This puzzle is unsolvable")
	}
	val time = measureTimeMillis { solvePuzzle(startBoard) }
	println("SOLVED".fontColor(GREEN))
	println("time: $time ms".fontColor(RED))
	println("time complexity: $timeComplexity")
	println("size complexity: $sizeComplexity")
	println("type: ${Board.heuristic}".fontColor(MAGENTA))
}

//https://uk.wikipedia.org/wiki/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_%D0%BF%D0%BE%D1%88%D1%83%D0%BA%D1%83_A*
fun solvePuzzle(startBoard: Board) {
	val startState = State(startBoard)
	openList.add(startState)

	while (openList.isNotEmpty()) {
		sizeComplexity = max(sizeComplexity, (openList.size + closedList.size).toLong())
		timeComplexity++
		val currentState = openList.poll()

		if (currentState.board.isSolved) {
			currentState.printTrace()
			break
		}
		expandState(currentState)
		closedList.add(currentState)
	}
}

fun expandState(state: State) {
	val states = state.getNeighbourStates()
	states.filter { !closedList.contains(it) }.forEach { openList.add(it) }
}

private fun parseInput(): Board {
	System.setIn(FileInputStream("src/input.txt"))

	val scanner = Scanner(System.`in`)
	var size: Int? = null
	val boardLines = ArrayList<String>()

	while (scanner.hasNextLine()) {
		var line = scanner.nextLine()
		if (line.contains('#')) {
			val commentLength = line.length - line.split('#')[0].length
			line = line.dropLast(commentLength)
		}
		if (line.isBlank()) {
			continue
		}
		if (size == null) {
			validate("Invalid size input") { size = line.toInt() }
			if (size!! < 3) {
				invalidExit("N-puzzle can\'t be smaller than 3x3")
			}
		} else {
			boardLines.add(line)
		}

	}
	Board.setBoardsSize(size!!)
	return Board.createBoard(boardLines)
}