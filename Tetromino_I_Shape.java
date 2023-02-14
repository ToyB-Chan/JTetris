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
				this.blocks[0].setRelativeLocation(0, 0);
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
	}
}