public class Tetromino_Z_Shape extends Tetromino {
	public Tetromino_Z_Shape() {
		this.blocks[0] = new TetrominoBlock(0, 0, this, TerminalColor.Red);
		this.blocks[1] = new TetrominoBlock(1, 0, this, TerminalColor.Red);
		this.blocks[1] = new TetrominoBlock(1, -1, this, TerminalColor.Red);
		this.blocks[1] = new TetrominoBlock(2, -1, this, TerminalColor.Red);
	}
}