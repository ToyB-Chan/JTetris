public class TetrominoLShape extends Tetromino {
	public TetrominoLShape() {
		this.blocks[0] = new TetrominoBlock(0, 0, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(0, 1, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(0, -1, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(-1, -1, this, TerminalColor.Cyan)
	}
}