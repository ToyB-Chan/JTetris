public class Tetromino_J_Shape extends Tetromino {
	public Tetromino_J_Shape() {
		this.blocks[0] = new TetrominoBlock(0, 0, this, TerminalColor.Blue);
		this.blocks[1] = new TetrominoBlock(0, -1, this, TerminalColor.Blue);
		this.blocks[1] = new TetrominoBlock(0, -2, this, TerminalColor.Blue);
		this.blocks[1] = new TetrominoBlock(-1, -2, this, TerminalColor.Blue);
	}
}