const val EMPTY_TILE = 0

class Board private constructor(private val board: ArrayList<MutableList<Int>>) {

	private val tiles = Tile.findAllTiles(board)

	private val emptyTile: Tile = tiles[EMPTY_TILE]!!

	val isSolved
		get() = board == solvedBoard.board

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

		var conflicts = 0
		for (leftTile in 0 until tilesInGoalRow.size)
			for (rightTile in leftTile + 1 until tilesInGoalRow.size)
				if (tilesHaveLinearRowConflict(tilesInGoalRow[leftTile], tilesInGoalRow[rightTile]))
					conflicts++
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
		var conflicts = 0
		for (topTile in 0 until tilesInGoalColumn.size)
			for (bottomTile in topTile + 1 until tilesInGoalColumn.size)
				if (tilesHaveLinearColumnConflict(tilesInGoalColumn[topTile], tilesInGoalColumn[bottomTile]))
					conflicts++
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

	val canMoveEmptyTileUp
		get() = emptyTile.y > 0

	val canMoveEmptyTileDown
		get() = emptyTile.y < size - 1

	val canMoveEmptyTileLeft
		get() = emptyTile.x > 0

	val canMoveEmptyTileRight
		get() = emptyTile.x < size - 1

	fun moveEmptyTileUp() {
		board[emptyTile.y][emptyTile.x] = board[emptyTile.y - 1][emptyTile.x]
		board[emptyTile.y - 1][emptyTile.x] = EMPTY_TILE
		emptyTile.y--
	}

	fun moveEmptyTileDown() {
		board[emptyTile.y][emptyTile.x] = board[emptyTile.y + 1][emptyTile.x]
		board[emptyTile.y + 1][emptyTile.x] = EMPTY_TILE
		emptyTile.y++
	}

	fun moveEmptyTileLeft() {
		board[emptyTile.y][emptyTile.x] = board[emptyTile.y][emptyTile.x - 1]
		board[emptyTile.y][emptyTile.x - 1] = EMPTY_TILE
		emptyTile.x--
	}

	fun moveEmptyTileRight() {
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
		println("----------------")
		for(row in board) {
			for (tile in row) {
				print(" $tile")
			}
			println()
		}
		println("----------------")
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

	companion object {

		private var size = 0

		lateinit var solvedBoard: Board

		var heuristic: Heuristic = Heuristic.HAMMING

		fun setBoardsSize(size: Int) {
			this.size = size
			solvedBoard = Board(SolvedPuzzleGenerator(size).generate())
		}

		fun createBoard(rows: ArrayList<String>): Board {
			val board = ArrayList<MutableList<Int>>()
			validate("Invalid n-puzzle format") {
				rows.forEach { row -> board.add(row.split("\\s+".toRegex()).filter { it != "" }.map { it.toInt() } as MutableList<Int>) }
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