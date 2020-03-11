import java.util.*

const val INPUT =
			"1 2 3\n" +
			"8 6 4\n" +
			"7 0 5\n"

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
			pathIsFound()
		}
		expandState(currentState)
		closedList.add(currentState)
	}
}

fun pathIsFound() {
	println("SOLVED")
}

fun expandState(state: State) {

}