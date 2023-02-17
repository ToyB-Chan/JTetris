public class TetrominoBlock extends GameObject {
	TerminalColor color;

	public TetrominoBlock(int x, int y, GameObject parent, TerminalColor color) {
		this.relativeLocationX = x;
		this.relativeLocationY = y;
		this.parent = parent;
		this.color = color;
	}

	public TetrominoBlock copy() {
		return new TetrominoBlock(this.relativeLocationX, this.relativeLocationY, this.parent, this.color);
	}

	@Override
	public void draw(TerminalCanvas canvas) {
		canvas.drawString(this.getAbsoluteLocationX(), this.getAbsoluteLocationY(), "L", TerminalColor.WHITE, this.color);
	}
}