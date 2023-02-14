public abstract class Tetromino extends GameObject {
	public TetrominoBlock[] blocks;
	protected int rotation = 0;

	public abstract void setRotation(int rotation);
	public int getRotation() { return this.rotation; }

	@Override
	public void draw(TerminalCanvas canvas) {
		for (int i = 0 ; i < blocks.length; i++) {
			this.blocks[i].draw(canvas);
		}

		canvas.drawPixel(relativeLocationX, relativeLocationY, TerminalColor.Red);
	}
}