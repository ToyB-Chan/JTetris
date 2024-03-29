public class Tetromino_O_Shape extends Tetromino {
	public Tetromino_O_Shape(int rotation) {
		super(rotation, 4, TerminalColor.YELLOW);
	}

	@Override
	public void setRotation(int rotation) {
		this.rotation = rotation;

		switch (rotation) {
			case 0:
			case 1:
			case 2:
			case 3:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, -1);
				this.blocks[2].setRelativeLocation(1, 0);
				this.blocks[3].setRelativeLocation(1, -1);
				break;
		}
	}

	@Override
	public Tetromino copy() {
		Tetromino cTetromino = new Tetromino_O_Shape(rotation);
		cTetromino.parent = this.parent;
		cTetromino.relativeLocationX = this.relativeLocationX;
		cTetromino.relativeLocationY = this.relativeLocationY;
		return cTetromino; 
	}
}