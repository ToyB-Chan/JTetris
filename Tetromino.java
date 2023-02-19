public abstract class Tetromino extends GameObject {
	protected static final int LAST_INDEX = 6;
	public TetrominoBlock[] blocks;
	protected int rotation = 0;

	public Tetromino(int rotation, int blockCount, TerminalColor blockColor) {
		this.blocks  = new TetrominoBlock[blockCount];
		for (int i = 0; i < blockCount; i++) { 
			this.blocks[i] = new TetrominoBlock(0, 0, this, blockColor); 
		}

		this.setRotation(rotation);	
	}

	public abstract void setRotation(int rotation);
	public int getRotation() { return this.rotation; }

	@Override
	public void draw(TerminalCanvas canvas) {
		for (int i = 0 ; i < blocks.length; i++) {
			this.blocks[i].draw(canvas);
		}

		//canvas.drawPixel(this.getAbsoluteLocationX(), this.getAbsoluteLocationY(), TerminalColor.Red);
	}

	public static Tetromino newTetrominoFromIndex(int index, int rotation) {
		// clamp index between 0 - LAST_INDEX
		index = index > LAST_INDEX ? LAST_INDEX : index < 0 ? 0 : index;

		switch (index) {
			case 0: return new Tetromino_I_Shape(rotation);
			case 1: return new Tetromino_J_Shape(rotation);
			case 2: return new Tetromino_L_Shape(rotation);
			case 3: return new Tetromino_O_Shape(rotation);
			case 4: return new Tetromino_S_Shape(rotation);
			case 5: return new Tetromino_T_Shape(rotation);
			case 6: return new Tetromino_Z_Shape(rotation);
			default: return null; // Should never happen
		}
	}
}