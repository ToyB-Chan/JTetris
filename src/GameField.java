import java.util.Random;

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
			boolean isBlockerRow = false;

			for (int ix = 0; ix < this.width; ix++) {
				isRowFull = isRowFull && this.grid[ix][iy] != null;
				isBlockerRow = isBlockerRow || (this.grid[ix][iy] != null && this.grid[ix][iy].isBlocker);
			}

			if (isRowFull) {

				if (!isBlockerRow) {
					rowsRemoved++;
				}

				for(int fx = 0; fx < this.width; fx++){
					this.grid[fx][iy] = null;
				}

				for(int py = iy ; py > 0 ; py--){
					for (int fx = 0 ; fx < this.width; fx++){
						this.grid[fx][py] = this.grid[fx][py-1];

						if (this.grid[fx][py] != null) {
							this.grid[fx][py].relativeLocationY++;
						}
					}

				}	
			}
		}

		return rowsRemoved;
	}

	public void addBlockingRow() {
		for (int ix = 0; ix < this.width; ix++) {
			for (int iy = 1; iy < this.height; iy++) {
				if (this.grid[ix][iy] != null) {
					this.grid[ix][iy].relativeLocationY--;
				} 

				this.grid[ix][iy-1] = this.grid[ix][iy];
			}
		}

		Random rand = new Random();
		for (int x = 0; x < this.width; x++) {
			
			if (rand.nextInt(this.width) > 1) { 
				TetrominoBlock block = new TetrominoBlock(x, this.height - 1, this, TerminalColor.DARK_GRAY);
				block.isBlocker = true;
				this.grid[x][this.height - 1] = block;
			}
			else {
				this.grid[x][this.height - 1] = null;
			}
		}
	}

	public int width() { return this.width; }
	public int height() { return this.height; }
}

