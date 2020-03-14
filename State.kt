
class State(val board: Board) {

	var g = 0
	var h = 0
	var f = 0

	private var parent: State? = null

	fun getNeighbourStates(): ArrayList<State> {
		val states = ArrayList<State>()
		with(board) {
			if (canMoveEmptyTileUp) states.add(generateNeighbourState(Board::moveEmptyTileUp))
			if (canMoveEmptyTileDown) states.add(generateNeighbourState(Board::moveEmptyTileDown))
			if (canMoveEmptyTileLeft) states.add(generateNeighbourState(Board::moveEmptyTileLeft))
			if (canMoveEmptyTileRight) states.add(generateNeighbourState(Board::moveEmptyTileRight))
		}
		return states
	}

	private fun generateNeighbourState(moveFunc: Board.() -> Unit): State {
		val newBoard = board.copy()
		newBoard.moveFunc()
		val state = State(newBoard)
		state.g = g + 1
		state.h = newBoard.heuristicScore
		state.f = state.g + state.h
		state.parent = this
		return state
	}

	override fun equals(other: Any?): Boolean {
		if (other is State) {
			return board == other.board
		}
		return false
	}

	fun print() {
		println("g: $g, h: $h, f: $f")
		board.print()
	}

	fun printTrace() {
		var currentState: State? = this
		val trace = arrayListOf<State>()

		while (currentState != null) {
			trace.add(currentState)
			currentState = currentState.parent
		}

		trace.reverse()
		trace.forEach { it.print() }
	}
}