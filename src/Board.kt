
const val EMPTY_TILE = 0

class Board private constructor(private val board: ArrayList<MutableList<Int>>) {

	private val tiles = Tile.findAllTiles(board, tilesAmount)

	private val emptyTile: Tile = tiles[EMPTY_TILE]!!

	val heuristicHamming
		get() = tiles.values.count { tile -> !tile.isOnSamePlace(solvedBoard.tiles[tile.value]!!) }

	val heuristicManhattan: Int
		get() = tiles.values.sumBy { tile -> tile.manhattanDistance(solvedBoard.tiles[tile.value]!!) }

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

	val isSolved
		get() = board == solvedBoard.board

	override fun equals(other: Any?): Boolean {
		if (other is Board) {
			return board == other.board
		}
		return false
	}

	companion object {

		private var size = 0
		private var tilesAmount = 0
		private var solvedBoard = Board(ArrayList<MutableList<Int>>().apply {
			add(mutableListOf(1, 2, 3))
			add(mutableListOf(8, 0, 4))
			add(mutableListOf(7, 6, 5))
		})

		fun setBoardsSize(size: Int) {
			this.size = size
			tilesAmount = size * size
			//TODO: generate solved board
		}

		fun createBoard(input: String): Board {
			val board = ArrayList<MutableList<Int>>()
			val rows = input.split("\n").dropLast(1);
			rows.forEach { row -> board.add(row.split("\\s+".toRegex()).filter { it != "" }.map { it.toInt() } as MutableList<Int>) }
			return Board(board)
		}
	}
}
