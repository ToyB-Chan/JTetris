public class TetrominoBlock extends GameObject {
	TerminalColor color;

	public TetrominoBlock(int x, int y, GameObject parent, TerminalColor color) {
		this.relativeLocationX = x;
		this.relativeLocationY = y;
		this.parent = parent;
		this.color = color;
	}
}