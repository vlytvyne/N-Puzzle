
class State(private val board: Board, private val parent: State? = null) {

	val g: Int = if (parent == null) 0 else parent.g + 1
	var h = board.heuristicScore
	var f = g + h

	val isSolved
		get() = board.isSolved

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
		return State(newBoard, this)
	}

	fun print() {
		if (CommandLineConfig.verboseOutput) {
			printVerboseInfo()
		} else {
			println("---------------------".fontColor(AnsiColor.BLUE))
		}
		board.print()
	}

	private fun printVerboseInfo() {
		val lastMoveInfo = when (board.lastEmptyTileMove) {
			TileMove.UP -> "⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆"
			TileMove.DOWN -> "⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇"
			TileMove.LEFT -> "⬅⬅⬅⬅⬅⬅⬅⬅⬅⬅"
			TileMove.RIGHT -> "⮕⮕⮕⮕⮕⮕⮕⮕⮕⮕"
			null -> "    --- START ---"
		}
		println("---------------------".fontColor(AnsiColor.MAGENTA))
		println(lastMoveInfo)
		println("---------------------".fontColor(AnsiColor.MAGENTA))
		println("---------------------".fontColor(AnsiColor.BLUE))
		println("g: $g, h: $h, f: $f")
		println("---------------------".fontColor(AnsiColor.BLUE))
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

	override fun equals(other: Any?): Boolean {
		if (other is State) {
			return board == other.board
		}
		return false
	}

	override fun hashCode(): Int {
		return board.hashCode()
	}
}