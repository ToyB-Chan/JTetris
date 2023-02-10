public abstract class Tetromino extends GameObject {
	public TetrominoBlock[] blocks;

	@Override
	public void draw(TerminalCanvas canvas) {
		for (int i = 0 ; i < blocks.length; i++) {
			this.blocks[i].draw(canvas);
		}
	}
}