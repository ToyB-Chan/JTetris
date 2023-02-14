public class Tetromino_I_Shape extends Tetromino {
	public Tetromino_I_Shape(int rotation) {
		this.blocks  = new TetrominoBlock[4];
		for (int i = 0; i < 3; i++) { 
			this.blocks[i] = new TetrominoBlock(0, 0, this, TerminalColor.Cyan); 
		}

		this.setRotation(rotation);	
	}

	@Override
	public void setRotation(int rotation) {
		this.rotation = rotation;

		switch (rotation) {
			case 0:
			case 2:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, -1);
				this.blocks[2].setRelativeLocation(0, -2);
				this.blocks[3].setRelativeLocation(0, -3);
				break;
			case 1:
			case 3: 
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(1, 0);
				this.blocks[2].setRelativeLocation(2, 0);
				this.blocks[3].setRelativeLocation(3, 0);
				break;
		}
	}
}