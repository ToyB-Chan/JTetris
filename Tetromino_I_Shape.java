public class Tetromino_I_Shape extends Tetromino {
	public Tetromino_I_Shape() {
		this.blocks[0] = new TetrominoBlock(0, 0, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(0, -1, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(0, -2, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(0, -3, this, TerminalColor.Cyan)
	}
}