const val EMPTY_TILE = 0

class Board private constructor(private val board: ArrayList<MutableList<Int>>) {

	private val tiles = Tile.findAllTiles(board)

	private val emptyTile: Tile = tiles[EMPTY_TILE]!!

	val isSolved
		get() = board == solvedBoard.board

	//https://algorithmsinsight.wordpress.com/graph-theory-2/a-star-in-general/implementing-a-star-to-solve-n-puzzle/
	val heuristicScore
		get() = when (heuristic) {
			Heuristic.HAMMING -> heuristicHammingScore
			Heuristic.MANHATTAN -> heuristicManhattanScore
			Heuristic.LINEAR_CONFLICT -> heuristicLinearConflictScore
		}

	private val heuristicHammingScore
		get() = tiles.values.filter { it != emptyTile }.count { tile -> !tile.isOnSamePlace(solvedBoard.tiles[tile.value]!!) }

	private val heuristicManhattanScore
		get() = tiles.values.filter { it != emptyTile }.sumBy { tile -> tile.manhattanDistance(solvedBoard.tiles[tile.value]!!) }

	private val heuristicLinearConflictScore
		get() = heuristicManhattanScore + getLinearConflictsOnBoard() * 2

	private fun getLinearConflictsOnBoard() =
		(0 until size).sumBy { countLinearConflictsInRow(it) + countLinearConflictsInColumn(it)}

	private fun countLinearConflictsInRow(row: Int): Int {
		val tilesInGoalRow = board[row].filter { it != EMPTY_TILE }.map { tiles[it]!! }.filter { isTileInGoalRow(it) }

		val conflicts = tilesInGoalRow.indices.sumBy {
				leftTile -> (leftTile + 1 until tilesInGoalRow.size).count {
				rightTile -> tilesHaveLinearRowConflict(tilesInGoalRow[leftTile], tilesInGoalRow[rightTile])
			}
		}
		return conflicts
	}

	private fun isTileInGoalRow(tile: Tile): Boolean {
		val solvedTile = solvedBoard.tiles[tile.value]!!
		return solvedTile.y == tile.y
	}

	private fun tilesHaveLinearRowConflict(leftTile: Tile, rightTile: Tile): Boolean {
		val solvedLeftTile = solvedBoard.tiles[leftTile.value]!!
		return solvedLeftTile.x >= rightTile.x
	}

	private fun countLinearConflictsInColumn(column: Int): Int {
		val tilesInGoalColumn = board.map { it[column] }.filter { it != EMPTY_TILE }.map { tiles[it]!! }.filter { isTileInGoalColumn(it) }

		val conflicts = tilesInGoalColumn.indices.sumBy {
				topTile -> (topTile + 1 until tilesInGoalColumn.size).count {
				bottomTile -> tilesHaveLinearColumnConflict(tilesInGoalColumn[topTile], tilesInGoalColumn[bottomTile])
			}
		}
		return conflicts
	}

	private fun isTileInGoalColumn(tile: Tile): Boolean {
		val solvedTile = solvedBoard.tiles[tile.value]!!
		return solvedTile.x == tile.x
	}

	private fun tilesHaveLinearColumnConflict(topTile: Tile, bottomTile: Tile): Boolean {
		val solvedTopTile = solvedBoard.tiles[topTile.value]!!
		return solvedTopTile.y >= bottomTile.y
	}

	var lastEmptyTileMove: TileMove? = null

	val canMoveEmptyTileUp
		get() = emptyTile.y > 0

	val canMoveEmptyTileDown
		get() = emptyTile.y < size - 1

	val canMoveEmptyTileLeft
		get() = emptyTile.x > 0

	val canMoveEmptyTileRight
		get() = emptyTile.x < size - 1

	fun moveEmptyTileUp() {
		lastEmptyTileMove = TileMove.UP
		board[emptyTile.y][emptyTile.x] = board[emptyTile.y - 1][emptyTile.x]
		board[emptyTile.y - 1][emptyTile.x] = EMPTY_TILE
		emptyTile.y--
	}

	fun moveEmptyTileDown() {
		lastEmptyTileMove = TileMove.DOWN
		board[emptyTile.y][emptyTile.x] = board[emptyTile.y + 1][emptyTile.x]
		board[emptyTile.y + 1][emptyTile.x] = EMPTY_TILE
		emptyTile.y++
	}

	fun moveEmptyTileLeft() {
		lastEmptyTileMove = TileMove.LEFT
		board[emptyTile.y][emptyTile.x] = board[emptyTile.y][emptyTile.x - 1]
		board[emptyTile.y][emptyTile.x - 1] = EMPTY_TILE
		emptyTile.x--
	}

	fun moveEmptyTileRight() {
		lastEmptyTileMove = TileMove.RIGHT
		board[emptyTile.y][emptyTile.x] = board[emptyTile.y][emptyTile.x + 1]
		board[emptyTile.y][emptyTile.x + 1] = EMPTY_TILE
		emptyTile.x++
	}

	fun copy(): Board {
		val bb = ArrayList<MutableList<Int>>()
		board.forEach { bb.add(it.toMutableList()) }
		return Board(bb)
	}

	fun print() {
		val lastMoveInfo = when (lastEmptyTileMove) {
			TileMove.UP -> "⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆"
			TileMove.DOWN -> "⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇"
			TileMove.LEFT -> "⬅⬅⬅⬅⬅⬅⬅⬅⬅⬅"
			TileMove.RIGHT -> "⮕⮕⮕⮕⮕⮕⮕⮕⮕⮕"
			null -> "--- START ---"
		}
		println(lastMoveInfo)
		for(row in board) {
			for (tile in row) {
				if (tile == EMPTY_TILE) {
					print("%3d ".format(tile).fontColor(AnsiColor.BLUE))
				} else {
					print("%3d ".format(tile))
				}
			}
			println()
		}
	}

	override fun equals(other: Any?): Boolean {
		if (other is Board) {
			return board == other.board
		}
		return false
	}

	override fun hashCode(): Int {
		return board.hashCode()
	}

	//https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
	val isSolvable: Boolean
		get() {
			val tilesInUsualOrder = board.flatMap { it.toList() }.filter { it != EMPTY_TILE }.map { usualTilesPositions[it]!! }
			val inversions = countInversions(tilesInUsualOrder)
			return if (size.isEven) {
				isEvenWidthBoardSolvable(inversions)
			} else {
				isOddWidthBoardSolvable(inversions)
			}
		}

	private fun countInversions(tilesInUsualOrder: List<Int>) =
		tilesInUsualOrder.indices.sumBy {
				leftTile -> (leftTile + 1 until tilesInUsualOrder.size).count {
				rightTile -> tilesInUsualOrder[leftTile] > tilesInUsualOrder[rightTile]
			}
		}

	private fun isEvenWidthBoardSolvable(inversions: Int): Boolean {
		val emptyTileInSolvedBoardIsOddFromBottom = size % 4 != 0
		return if (emptyTileInSolvedBoardIsOddFromBottom) {
			if (emptyTile.y.isEven) inversions.isOdd else inversions.isEven
		} else {
			if (emptyTile.y.isEven) inversions.isEven else inversions.isOdd
		}
	}

	private fun isOddWidthBoardSolvable(inversions: Int) = inversions.isEven

	companion object {

		private var size = 0

		private lateinit var solvedBoard: Board
		private val usualTilesPositions = HashMap<Int, Int>()

		var heuristic: Heuristic = Heuristic.HAMMING

		fun setBoardsSize(size: Int) {
			this.size = size
			solvedBoard = Board(SolvedPuzzleGenerator(size).generate())
			solvedBoard.board.flatMap { it.toList() }.forEachIndexed { index, tile -> usualTilesPositions[tile] = index }
		}

		fun createBoard(rows: ArrayList<String>): Board {
			val board = ArrayList<MutableList<Int>>()
			try {
				rows.forEach { row -> board.add(row.split("\\s+".toRegex()).filter { it != "" }.map { it.toInt() } as MutableList<Int>) }
			} catch (e: Exception) {
				invalidExit("Invalid n-puzzle format")
			}
			val distinctTilesAmount = board.flatMap { it.toList() }.distinct().size
			when {
				board.size != size -> invalidExit("Invalid n-puzzle size")
				board.any { row -> row.size != size } -> invalidExit("Invalid n-puzzle size")
				distinctTilesAmount != size * size -> invalidExit("N-puzzle has tiles duplicates")
			}
			return Board(board)
		}
	}
}

enum class Heuristic {
	HAMMING,
	MANHATTAN,
	LINEAR_CONFLICT
}

enum class TileMove {
	UP,
	DOWN,
	LEFT,
	RIGHT
}