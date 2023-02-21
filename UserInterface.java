public class UserInterface extends GameObject {
	//private TetrominoBlock[][] grid;
	public int width =10;
	public int height =20;
	public int time = 0;
	public float speed = 0.01f;

	public UserInterface (){
	//this.width = width;
	//this.height = height;
	//this.grid = new TetrominoBlock[width][];
	}


	@Override
	public void draw(TerminalCanvas canvas) {
		time += Main.TARGET_FRAME_TIME;
		//canvas.drawArea(9, 10, 1, 20, 'L', TerminalColor.TRANSPARENT, TerminalColor.BLUE);
		for (int i = 10; i < 30; i++) {
			canvas.drawPixel(9, i, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + i) * 255), (int)(Math.sin(time * speed + 50 + i) * 200) ));
			canvas.drawPixel(20, i, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + i) * 255), (int)(Math.sin(time * speed + 50 + i) * 200) ));
		}
		for (int j = 9; j < 21; j++) {
			canvas.drawPixel(j, 9, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + j) * 255), (int)(Math.sin(time * speed + 50 + j) *200) ));
			canvas.drawPixel(j, 30, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + j ) * 255), (int)(Math.sin(time * speed + 50 + j) * 200) ));
		}
		
}
}	



