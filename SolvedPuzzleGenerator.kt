const val NOT_SET = -1
typealias Puzzle = ArrayList<MutableList<Int>>

class SolvedPuzzleGenerator(private val size: Int) {
	private var xPos = 0
	private var yPos = 0
	private var currentTileValue = 1
	var puzzle: Puzzle = Puzzle().apply {
		repeat(this@SolvedPuzzleGenerator.size) { this.add(MutableList(this@SolvedPuzzleGenerator.size) { NOT_SET }) }
	}

	private val isCurrentTileNotSet
		get() = puzzle[yPos][xPos] == NOT_SET

	private val isCurrentTileTheLast
		get() = currentTileValue == size * size

	fun generate(): Puzzle {
		goRight()
		return puzzle
	}

	private fun goRight() {
		while (xPos < size) {
			if (isCurrentTileNotSet) {
				if (isCurrentTileTheLast) {
					puzzle[yPos][xPos] = EMPTY_TILE
					return
				}
				puzzle[yPos][xPos++] = currentTileValue++
			} else {
				break
			}
		}
		xPos--
		yPos++
		goDown()
	}

	private fun goDown() {
		while (yPos < size) {
			if (isCurrentTileNotSet) {
				if (isCurrentTileTheLast) {
					puzzle[yPos][xPos] = 0
					return
				}
				puzzle[yPos++][xPos] = currentTileValue++
			} else {
				break
			}
		}
		yPos--
		xPos--
		goLeft()
	}

	private fun goLeft() {
		while (xPos >= 0) {
			if (isCurrentTileNotSet) {
				if (isCurrentTileTheLast) {
					puzzle[yPos][xPos] = 0
					return
				}
				puzzle[yPos][xPos--] = currentTileValue++
			} else {
				break
			}
		}
		xPos++
		yPos--
		goUp()
	}

	private fun goUp() {
		while (yPos >= 0) {
			if (isCurrentTileNotSet) {
				if (isCurrentTileTheLast) {
					puzzle[yPos][xPos] = 0
					return
				}
				puzzle[yPos--][xPos] = currentTileValue++
			} else {
				break
			}
		}
		yPos++
		xPos++
		goRight()
	}
}