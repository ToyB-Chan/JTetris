public class TetrominoBlock extends GameObject {
	public TerminalColor baseColor;
	public TerminalColor highlightColor;

	public TetrominoBlock(int x, int y, GameObject parent, TerminalColor color) {
		this.relativeLocationX = x;
		this.relativeLocationY = y;
		this.parent = parent;
		this.baseColor = color;
		this.generateHighlightColor();
	}

	public TetrominoBlock copy() {
		return new TetrominoBlock(this.relativeLocationX, this.relativeLocationY, this.parent, this.baseColor);
	}

	public void generateHighlightColor() {
		this.highlightColor = new TerminalColor(this.baseColor.R + 32, this.baseColor.G + 32, this.baseColor.B + 32);
	}

	@Override
	public void draw(TerminalCanvas canvas) {
		canvas.drawString(this.getAbsoluteLocationX(), this.getAbsoluteLocationY(), "L", this.highlightColor, this.baseColor);
	}
}