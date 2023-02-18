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
			TetrominoBlock block = tetromino.blocks[i];
			if (block.relativeLocationX + x >= this.width || block.relativeLocationY + y >= this.height || block.relativeLocationX + x < 0 || block.relativeLocationY + y < 0) {
				return false;
			}

			if (this.grid[block.relativeLocationX + x][block.relativeLocationY + y] != null) {
				return false;
			}
		}

		return true;
	}

	public void addTetromino(int x, int y, Tetromino tetromino) {
		for (int i = 0; i < tetromino.blocks.length; i++) {
			TetrominoBlock block = tetromino.blocks[i].copy();
			block.relativeLocationX += x;
			block.relativeLocationY += y;
			block.parent = this;
			this.grid[block.relativeLocationX][block.relativeLocationY] = block;
		}

		tetromino.blocks = null;
	}

	@Override
	public void draw(TerminalCanvas canvas) {
		for (int x = 0; x < this.grid.length; x++) {
			for (int y = 0; y < this.grid[x].length; y++) {
				if (this.grid[x][y] == null) {
					canvas.drawString(this.getAbsoluteLocationX() + x, this.getAbsoluteLocationY() + y, ".", TerminalColor.DARK_GRAY, TerminalColor.TRANSPARENT);	
				} else {
					this.grid[x][y].draw(canvas);
				}
			}
		}
	}

	public int removeFullRows(){
		int rowsRemoved = 0;

		for(int iy = 0; iy < this.height; iy++) {
			boolean isRowFull = true;

			for (int ix = 0; ix < this.width; ix++) {
				isRowFull = isRowFull && this.grid[ix][iy] != null;
			}

			if (isRowFull) {
				rowsRemoved++;

			}
		}

		return rowsRemoved;
	}
}
