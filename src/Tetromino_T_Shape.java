public class Tetromino_T_Shape extends Tetromino {
	public Tetromino_T_Shape(int rotation) {
		super(rotation, 4, TerminalColor.MAGENTA);
	}

	@Override
	public void setRotation(int rotation) {
		this.rotation = rotation;

		switch (rotation) {
			case 0:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(-1, 0);
				this.blocks[2].setRelativeLocation(1, 0);
				this.blocks[3].setRelativeLocation(0, 1);
				break;
			case 1:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, 1);
				this.blocks[2].setRelativeLocation(0, -1);
				this.blocks[3].setRelativeLocation(-1, 0);
				break;
			case 2:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(-1, 0);
				this.blocks[2].setRelativeLocation(1, 0);
				this.blocks[3].setRelativeLocation(0, -1);	
				break;
			case 3:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, 1);
				this.blocks[2].setRelativeLocation(0, -1);
				this.blocks[3].setRelativeLocation(1, 0);
				break;
		}
	}

	@Override
	public Tetromino copy() {
		Tetromino cTetromino = new Tetromino_T_Shape(rotation);
		cTetromino.parent = this.parent;
		cTetromino.relativeLocationX = this.relativeLocationX;
		cTetromino.relativeLocationY = this.relativeLocationY;
		return cTetromino; 
	}
}