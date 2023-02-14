public class Tetromino_S_Shape extends Tetromino {
	public Tetromino_S_Shape(int rotation) {
		this.blocks  = new TetrominoBlock[4];
		for (int i = 0; i < 4; i++) { 
			this.blocks[i] = new TetrominoBlock(0, 0, this, TerminalColor.Green); 
		}

		this.setRotation(rotation);	
	}

	@Override
	public void setRotation(int rotation) {
		this.rotation = rotation;

		switch (rotation) {
			case 0:
			case 2:
				this.blocks[0].setRelativeLocation(1, 0);
				this.blocks[1].setRelativeLocation(2, 0);
				this.blocks[2].setRelativeLocation(0, -1);
				this.blocks[3].setRelativeLocation(1, -1);
				break;
			case 1:
			case 3:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, -1);
				this.blocks[2].setRelativeLocation(1, -1);
				this.blocks[3].setRelativeLocation(1, -2);
				break;
		}
	}
}