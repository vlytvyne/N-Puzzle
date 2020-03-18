import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

const val INPUT =
			" 1 18  2  4  5\n" +
			"17 15  3 19 20\n" +
			" 0 23 24  7  6\n" +
			"16 12 22 21  8\n" +
			"14 13 11 10  9\n"

const val SIZE = 5

val comparator = Comparator<State> {
	state1, state2 ->
	if (state1.f == state2.f) {
		return@Comparator state1.h - state2.h
	}
	return@Comparator state1.f - state2.f
}

val openList = PriorityQueue<State>(comparator)
val closedList = arrayListOf<State>()

fun main() {
	Board.setHeuristic(Heuristic.HAMMING)
	val startBoard = parseInput()
	startBoard.print()
	solvePuzzle(startBoard)
}

fun solvePuzzle(startBoard: Board) {
	val startState = State(startBoard)
	openList.add(startState)

	while (openList.isNotEmpty()) {
		val currentState = openList.poll()

		if (currentState.board.isSolved) {
			pathIsFound(currentState)
			break
		}
		expandState(currentState)
		closedList.add(currentState)
	}
	println("Path can\'t be found")
}

fun pathIsFound(state: State) {
	state.printTrace()
	println("SOLVED")
	exitProcess(0)
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
	Board.setHeuristic(Heuristic.HAMMING)
	return Board.createBoard(boardLines)
}