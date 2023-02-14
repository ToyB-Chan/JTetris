public class Tetromino_L_Shape extends Tetromino {
	public Tetromino_L_Shape(int rotation) {
		this.blocks = new TetrominoBlock[4];
		for (int i = 0; i < 3; i++) { 
			this.blocks[i] = new TetrominoBlock(0, 0, this, TerminalColor.DarkYellow);

		}
		this.setRotation(rotation);
	}

	public void setRotation(int rotation) {
		switch(rotation){
			case 0:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, -1);
				this.blocks[2].setRelativeLocation(0, -2);
				this.blocks[3].setRelativeLocation(1, -2);
			case 1:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(-1, 0);
				this.blocks[2].setRelativeLocation(-2, 0);
				this.blocks[3].setRelativeLocation(-2, -1);
			case 2:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, 1);
				this.blocks[2].setRelativeLocation(0, 2);
				this.blocks[3].setRelativeLocation(-1, 2);
			case 3:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(1, 0);
				this.blocks[2].setRelativeLocation(2, 0);
				this.blocks[3].setRelativeLocation(2, 1);
		}
		
	}
}