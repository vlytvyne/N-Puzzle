import java.lang.Exception
import kotlin.math.abs

typealias TileMap = HashMap<Int, Tile>

class Tile private constructor(var x: Int, var y: Int, val value: Int) {

	fun isOnSamePlace(tile: Tile): Boolean {
		return x == tile.x && y == tile.y
	}

	fun manhattanDistance(tile: Tile): Int {
		return abs(x - tile.x) + abs(y - tile.y)
	}

	companion object {

		fun findAllTiles(board: List<List<Int>>, tilesAmount: Int): TileMap {
			val tiles = TileMap()
			var x = 0
			var y = 0

			while (y < SIZE) {
				while (x < SIZE) {
					val tileValue = board[y][x]
					tiles[tileValue] = Tile(x, y, tileValue)
					x++
				}
				x = 0
				y++
			}
			return tiles
		}
	}
}