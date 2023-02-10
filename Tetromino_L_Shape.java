public class Tetromino_L_Shape extends Tetromino {
	public Tetromino_L_Shape() {
		this.blocks = new TetrominoBlock[4];
		this.blocks[0] = new TetrominoBlock(0, 0, this, TerminalColor.DarkRed);
		this.blocks[1] = new TetrominoBlock(0, -1, this, TerminalColor.DarkRed);
		this.blocks[2] = new TetrominoBlock(0, -2, this, TerminalColor.DarkRed);
		this.blocks[3] = new TetrominoBlock(1, -2, this, TerminalColor.DarkRed);
	}
}