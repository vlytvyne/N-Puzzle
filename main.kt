import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

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
	Board.heuristic = Heuristic.MANHATTAN
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
	return Board.createBoard(boardLines)
}