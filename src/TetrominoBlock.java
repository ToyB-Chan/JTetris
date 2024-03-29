public class TetrominoBlock extends GameObject {
	public TerminalColor baseColor;
	public TerminalColor highlightColor;
	public boolean renderAsGhost = false;
	public boolean isBlocker = false;

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
		this.highlightColor = new TerminalColor(this.baseColor.R + 38, this.baseColor.G + 38, this.baseColor.B + 38);
	}

	@Override
	public void draw(TerminalCanvas canvas) {
		if (this.renderAsGhost) {
			canvas.drawString(this.getAbsoluteLocationX(), this.getAbsoluteLocationY(), "M", this.baseColor, TerminalColor.TRANSPARENT);
		} else {
			canvas.drawString(this.getAbsoluteLocationX(), this.getAbsoluteLocationY(), "L", this.highlightColor, this.baseColor);
		}
	}
}