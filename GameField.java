public class GameField extends GameObject {
	private int width;
	private int height;
	private TetrominoBlock[][] grid;
	
	public GameField(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new TetrominoBlock[width][height];
	}

	public boolean canTetrominoBePlaced(int x, int y, Tetromino tetromino) {
		for (int i = 0; i < tetromino.blocks.length; i++) {
			if (this.grid[x + tetromino.relativeLocationX][y + tetromino.relativeLocationY] != null) {
				return false;
			}
		}

		return true;
	}

	public void addTetromino(int x, int y, Tetromino tetromino) {
		for (int i = 0; i < tetromino.blocks.length; i++) {
			tetromino.blocks[i].relativeLocationX += x;
			tetromino.blocks[i].relativeLocationX += y;
			tetromino.blocks[i].parent = this;
			this.grid[x][y] = tetromino.blocks[i];
		}
	}
}
