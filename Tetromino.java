public abstract class Tetromino extends GameObject {
	public TetrominoBlock[] blocks;
	public abstract void rotateLeft();
	public abstract void rotateRight();

	@Override
	public void draw(TerminalCanvas canvas) {
		for (int i = 0 ; i < blocks.length; i++) {
			this.blocks[i].draw(canvas);
		}
	}
}