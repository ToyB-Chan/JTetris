public abstract class Tetromino extends GameObject {
	public TetrominoBlock[] blocks;
	public abstract void rotateLeft();
	public abstract void rotateRight();
}