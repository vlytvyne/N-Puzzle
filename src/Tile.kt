import kotlin.math.abs

typealias TileMap = HashMap<Int, Tile>

class Tile private constructor(var x: Int, var y: Int, val value: Int) {

	fun isOnSamePlace(tile: Tile): Boolean {
		return x == tile.x && y == tile.y
	}

	fun manhattanDistance(tile: Tile): Int {
		return abs(x - tile.x) + abs(y - tile.y)
	}

	override fun toString(): String {
		return "Value: $value, y: $y, x: $x"
	}

	companion object {

		fun findAllTiles(board: List<List<Int>>): TileMap {
			val tiles = TileMap()
			var x = 0
			var y = 0

			while (y < board.size) {
				while (x < board.size) {
					val tileValue = board[y][x]
					tiles[tileValue] = Tile(x, y, tileValue)
					x++
				}
				x = 0
				y++
			}
			board.size
			return tiles
		}
	}
}