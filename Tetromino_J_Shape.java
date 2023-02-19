public class Tetromino_J_Shape extends Tetromino {
	
	//Constructer 
	public Tetromino_J_Shape(int rotation) {
		super(rotation, 4, TerminalColor.DARK_BLUE);
	}


	public void setRotation(int rotation) {
		this.rotation = rotation;

		switch(rotation){
			case 0:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(1, 0);
				this.blocks[2].setRelativeLocation(-1, 0);
				this.blocks[3].setRelativeLocation(-1, -1);
				break;
			case 1:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, 1);
				this.blocks[2].setRelativeLocation(0, -1);
				this.blocks[3].setRelativeLocation(1, -1);
				break;
			case 2:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(-1, 0);
				this.blocks[2].setRelativeLocation(1, 0);
				this.blocks[3].setRelativeLocation(1, 1);
				break;
			case 3:
				this.blocks[0].setRelativeLocation(0, 0);
				this.blocks[1].setRelativeLocation(0, 1);
				this.blocks[2].setRelativeLocation(0, -1);
				this.blocks[3].setRelativeLocation(-1, 1);
				break;
		}
		
	}
}