class EmptyTile private constructor(var x: Int, var y: Int) {

	companion object {

		fun findEmptyTile(board: List<List<Int>>): EmptyTile {
			var x = 0;
			var y = 0;

			while (y < SIZE) {
				while (x < SIZE) {
					if (board[y][x] == 0) {
						return EmptyTile(x, y)
					}
					x++;
				}
				x = 0;
				y++;
			}
			return EmptyTile(0, 0)
		}
	}
}