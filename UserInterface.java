public class UserInterface extends GameObject {
	//private TetrominoBlock[][] grid;
	public int width =10;
	public int height =20;

	public UserInterface (){
	//this.width = width;
	//this.height = height;
	//this.grid = new TetrominoBlock[width][];
	}


	@Override
	public void draw(TerminalCanvas canvas) {
		canvas.drawArea(9, 10, 1, 20, 'L', TerminalColor.TRANSPARENT, TerminalColor.BLUE);
		canvas.drawPixel(12, 12, TerminalColor.DARK_RED);
		
	}
}	



