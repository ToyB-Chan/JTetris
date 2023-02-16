public abstract class Tetromino extends GameObject {
	public TetrominoBlock[] blocks;
	protected int rotation = 0;

	public abstract void setRotation(int rotation);
	public int getRotation() { return this.rotation; }
	public boolean isGarabge() { return this.blocks == null; }

	@Override
	public void draw(TerminalCanvas canvas) {
		for (int i = 0 ; i < blocks.length; i++) {
			this.blocks[i].draw(canvas);
		}

		canvas.drawPixel(this.getAbsoluteLocationX(), this.getAbsoluteLocationY(), TerminalColor.Red);
	}
}