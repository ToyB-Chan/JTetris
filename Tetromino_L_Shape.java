public class Tetromino_L_Shape extends Tetromino {
	public Tetromino_L_Shape() {
		this.blocks[0] = new TetrominoBlock(0, 0, this, TerminalColor.DarkYellow);
		this.blocks[1] = new TetrominoBlock(0, -1, this, TerminalColor.DarkYellow);
		this.blocks[1] = new TetrominoBlock(0, -2, this, TerminalColor.DarkYellow);
		this.blocks[1] = new TetrominoBlock(1, -2, this, TerminalColor.DarkYellow);
	}
}