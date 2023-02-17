public class TetrominoBlock extends GameObject {
	TerminalColor baseColor;
	TerminalColor highlightColor;

	public TetrominoBlock(int x, int y, GameObject parent, TerminalColor color) {
		this.relativeLocationX = x;
		this.relativeLocationY = y;
		this.parent = parent;
		this.baseColor = color;
		this.highlightColor = new TerminalColor(color.R + 32, color.G + 32, color.B + 32);
	}

	public TetrominoBlock copy() {
		return new TetrominoBlock(this.relativeLocationX, this.relativeLocationY, this.parent, this.baseColor);
	}

	@Override
	public void draw(TerminalCanvas canvas) {
		canvas.drawString(this.getAbsoluteLocationX(), this.getAbsoluteLocationY(), "L", this.highlightColor, this.baseColor);
	}
}