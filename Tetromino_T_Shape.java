public class Tetromino_T_Shape extends Tetromino {
	public Tetromino_T_Shape() {
		this.blocks[0] = new TetrominoBlock(0, 0, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(-1, 0, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(-2, 0, this, TerminalColor.Cyan);
		this.blocks[1] = new TetrominoBlock(-1, -1, this, TerminalColor.Cyan);
	}
}