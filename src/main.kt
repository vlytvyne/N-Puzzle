import java.util.*

const val INPUT =
			"2 5 8\n" +
			"7 0 3\n" +
			"4 1 6\n"

const val SIZE = 3

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
	Board.setBoardsSize(SIZE)
	val startBoard = Board.createBoard(INPUT)

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

}

fun pathIsFound(state: State) {
	state.printTrace()
	println("SOLVED")
}

fun expandState(state: State) {
	val states = state.getNeighbourStates()
	states.filter { !closedList.contains(it) }.forEach { openList.add(it) }
}